package com.mmorpg.mbdl.framework.thread;

import com.google.common.base.Preconditions;
import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;
import com.mmorpg.mbdl.framework.thread.interfaces.ITimeOutHashMap;
import com.mmorpg.mbdl.framework.thread.task.AbstractTask;
import com.mmorpg.mbdl.framework.thread.task.TaskQueue;

import java.io.Serializable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 业务线程池，以及所有与业务线程池关联的队列
 * @author sando
 * @param <K> 线程池中任务队列的主键类型
 * @param <V> 使用的线程池类型
 */
public class PoolExecutor<K extends Serializable, V extends ScheduledExecutorService> {
    /** 业务线程池 */
    private V businessThreadPool;
    /** 业务所有的任务队列 */
    private ITimeOutHashMap<K, TaskQueue<K>> businessThreadPoolTaskQueues;

    public PoolExecutor(V businessThreadPool, long timeout, TimeUnit timeUnit) {
        this.businessThreadPool = businessThreadPool;
        this.businessThreadPoolTaskQueues = new TimeOutCaffeineMap<>(timeout, timeUnit,
                (key) -> new TaskQueue<>(key, this));
    }

    /**
     * 根据dispatcherId获取相应的任务队列
     * @param dispatcherId
     * @return 相应的任务队列，如果dispatcherId为null，则返回null
     */
    public TaskQueue<K> getOrCreateTaskQueue(K dispatcherId){
        return businessThreadPoolTaskQueues.getOrCreate(dispatcherId);
    }

    public ScheduledFuture<?> executeTask(AbstractTask<? extends Dispatchable<K>, K> abstractTask){
        Preconditions.checkNotNull(abstractTask);
        abstractTask.setExecutor(this);
        return addTask(abstractTask);
    }

    /**
     * 添加同步任务
     * @param runnable 任务
     * @return ScheduledFuture可用于控制任务以及检查状态
     */
    public ScheduledFuture<?> addTask(AbstractTask<? extends Dispatchable<K>, K> runnable) {
        return businessThreadPool.schedule(runnable, 0, TimeUnit.NANOSECONDS);
    }
    /**
     * 添加延时执行任务
     * @param runnable 任务
     * @param delay 延迟时间
     * @param timeUnit 使用的时间单位
     * @return ScheduledFuture可用于控制任务以及检查状态
     */
    public ScheduledFuture<?> addDelayedTask(Runnable runnable, long delay, TimeUnit timeUnit) {
        return businessThreadPool.schedule(runnable,delay,timeUnit);
    }
    /**
     * 添加固定时间周期执行的任务
     * @param runnable AbstractDispatcherRunnable类型的任务
     * @param initDelay 初始化延迟
     * @param period 周期时间
     * @param timeUnit 时间单位
     * @return ScheduledFuture可用于控制任务以及检查状态
     */
    public ScheduledFuture<?> addFixedRateTask(Runnable runnable, long initDelay, long period, TimeUnit timeUnit) {
        return businessThreadPool.scheduleAtFixedRate(runnable,initDelay,period,timeUnit);
    }
}
