package com.mmorpg.mbdl.framework.thread.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mmorpg.mbdl.framework.thread.TimeOutCaffeineMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 业务线程池，以及所有与业务线程池关联的队列
 * @author sando
 */
@Component
public class BussinessPoolExecutor {
    /** 业务线程池 */
    private ScheduledThreadPoolExecutor businessThreadPool;
    /** 业务所有的任务队列 */
    private TimeOutCaffeineMap<Serializable, TaskQueue> businessThreadPoolTaskQueues;

    private int processors = Runtime.getRuntime().availableProcessors();
    // @Value("${server.config.thread.poolSize}")
    private int poolSize= (processors<=4)?processors*2:processors+8;
    // @Value("${server.config.taskQueue.timeout}") TODO 自带的不能注入long，自行实现新的注入方式
    private long timeout;
    @Value("${server.config.thread.name}")
    private String threadNameFommat;

    private static BussinessPoolExecutor self;
    public static BussinessPoolExecutor getIntance(){
        return self;
    }
    @PostConstruct
    private void init(){
        self = this;
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(threadNameFommat+"%2d").build();
        this.businessThreadPool = new ScheduledThreadPoolExecutor(poolSize,namedThreadFactory);
        this.businessThreadPoolTaskQueues = new TimeOutCaffeineMap<>(timeout, TimeUnit.MINUTES,()->
                new TaskQueue(businessThreadPool)
        );
    }

    public TaskQueue getOrCreateTaskQueue(Serializable dispatcherId){
        return businessThreadPoolTaskQueues.getOrCreate(dispatcherId);
    }

    public ScheduledFuture<?> executeTask(AbstractTask abstractTask){
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
        }
        return null;
    }

    public TimeOutCaffeineMap<Serializable, TaskQueue> getBusinessThreadPoolTaskQueues() {
        return businessThreadPoolTaskQueues;
    }
    /**
     * 添加同步任务
     * @param runnable 任务
     * @return ScheduledFuture可用于控制任务以及检查状态
     */
    private ScheduledFuture<?> addTask(AbstractTask runnable) {
        return businessThreadPool.schedule(runnable,0,TimeUnit.NANOSECONDS);
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
