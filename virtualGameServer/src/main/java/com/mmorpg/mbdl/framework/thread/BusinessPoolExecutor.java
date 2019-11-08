package com.mmorpg.mbdl.framework.thread;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mmorpg.mbdl.framework.thread.task.AbstractTask;
import com.mmorpg.mbdl.framework.thread.task.DelayedTask;
import com.mmorpg.mbdl.framework.thread.task.FixedRateTask;
import com.mmorpg.mbdl.framework.thread.task.TaskQueue;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 业务线程池，以及所有与业务线程池关联的队列
 * @author sando
 */
@Component
public class BusinessPoolExecutor {
    /** 业务线程池 */
    // TODO 深入学习netty后对比决定是否换用netty的线程池
    private EventExecutorGroup businessThreadPool;
    /** 业务所有的任务队列 */
    private ITimeOutHashMap<Serializable, TaskQueue> businessThreadPoolTaskQueues;

    private int processors = Runtime.getRuntime().availableProcessors();
    // @Value("${server.config.thread.poolSize}")
    private int poolSize= (processors<=4)?processors*2:processors+8;
    // 当任务队列timeout分钟没有写入后缓存失效
    // @Value("${server.config.taskQueue.timeout}") TODO 自带的不能注入long，自行实现新的注入方式
    private long timeout = 1;
    @Value("${server.config.thread.name}")
    private String threadNameFormat;

    private static BusinessPoolExecutor self;
    public static BusinessPoolExecutor getInstance(){
        return self;
    }
    @PostConstruct
    private void init(){
        self = this;
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(threadNameFormat +"%2d").build();
        this.businessThreadPool = new DefaultEventExecutorGroup(poolSize, namedThreadFactory);
        this.businessThreadPoolTaskQueues = new TimeOutCaffeineMap<>(timeout, TimeUnit.MINUTES,(key) ->
                new TaskQueue(businessThreadPool)
        );
    }

    /**
     * 根据dispatcherId获取相应的任务队列
     * @param dispatcherId
     * @return 相应的任务队列，如果dispatcherId为null，则返回null
     */
    public TaskQueue getOrCreateTaskQueue(Serializable dispatcherId){
        return businessThreadPoolTaskQueues.getOrCreate(dispatcherId);
    }

    public ScheduledFuture<?> executeTask(AbstractTask abstractTask){
        Preconditions.checkNotNull(abstractTask);
        switch (abstractTask.taskType()) {
            case TASK:{
                return addTask(abstractTask);
            }
            case DELAYED_TASK:{
                DelayedTask delayedTask = (DelayedTask)abstractTask;
                return addDelayedTask(abstractTask,delayedTask.getDelay(),delayedTask.getTimeUnit());
            }
            case FIXED_RATE_TASK:{
                FixedRateTask fixedRateTask = (FixedRateTask)abstractTask;
                return addFixedRateTask(abstractTask,fixedRateTask.getInitalDelay(),fixedRateTask.getPeriod(),fixedRateTask.getTimeUnit());
            }
            default:
                throw new IllegalArgumentException(String.format("此类型[%s]的任务没有合适的处理方法",abstractTask.getClass().getSimpleName()));
        }
    }

    public ITimeOutHashMap<Serializable, TaskQueue> getBusinessThreadPoolTaskQueues() {
        return businessThreadPoolTaskQueues;
    }
    /**
     * 添加同步任务
     * @param runnable 任务
     * @return ScheduledFuture可用于控制任务以及检查状态
     */
    private ScheduledFuture<?> addTask(AbstractTask runnable) {
        return businessThreadPool.schedule(runnable, 0, TimeUnit.NANOSECONDS);
    }
    /**
     * 添加延时执行任务
     * @param runnable 任务
     * @param delay 延迟时间
     * @param timeUnit 使用的时间单位
     * @return ScheduledFuture可用于控制任务以及检查状态
     */
    private ScheduledFuture<?> addDelayedTask(AbstractTask runnable, long delay, TimeUnit timeUnit) {
        return businessThreadPool.schedule(runnable,delay,timeUnit);
    }
    /**
     * 添加固定时间周期执行的任务
     * @param runnable AbstractDispatcherRunnable类型的任务
     * @param initalDelay 初始化延迟
     * @param period 周期时间
     * @param timeUnit 时间单位
     * @return ScheduledFuture可用于控制任务以及检查状态
     */
    private ScheduledFuture<?> addFixedRateTask(AbstractTask runnable, long initalDelay, long period, TimeUnit timeUnit) {
        return businessThreadPool.scheduleAtFixedRate(runnable,initalDelay,period,timeUnit);
    }
}
