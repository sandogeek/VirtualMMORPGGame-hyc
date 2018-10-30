package com.mmorpg.mbdl.framework.thread.task;

import com.google.common.collect.Lists;
import com.mmorpg.mbdl.framework.thread.TaskExecutorGroup;

import java.util.Queue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 任务队列<br>
 * 玩家的任务通常串行执行，所以每个玩家一个队列,如果模块串行执行，也可以给模块一个队列
 * @author sando
 */
public class TaskQueue {
    // 与此任务队列相关的线程池
    private final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    // 存放任务的队列
    private Queue<AbstractTask> queue;

    public TaskQueue(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        this.scheduledThreadPoolExecutor = scheduledThreadPoolExecutor;
        this.queue = Lists.newLinkedList();
    }

    /**
     * 往任务队列提交一个任务,统一业务线程池操作的入口
     * @return
     */
    public ScheduledFuture<?> submit(AbstractTask abstractTask){
        // 固定频率执行的任务直接加到线程池中
        if (abstractTask.taskType()==TaskType.FIXED_RATE_TASK) {
            FixedRateTask fixedRateTask = (FixedRateTask)abstractTask;
            return addFixedRateTask(abstractTask,fixedRateTask.getInitalDelay(),fixedRateTask.getPeriod(),fixedRateTask.getTimeUnit());
        }
        synchronized (this){
            this.queue.add(abstractTask);
            if (queue.size()==1){
                // 只有一个任务的话，说明是刚加的，立即送到线程池里的队列执行
                return executeTask(abstractTask);
            }
        }
        return null;
    }

    /**
     * 执行完一个任务后的处理
     * @return
     */
    public ScheduledFuture<?> andThen() {
        synchronized (this){
            // 移除执行完毕的任务
            queue.poll();
            if (!queue.isEmpty()) {
                // 有任务继续执行
                return executeTask(queue.poll());
            }
        }
        return null;
    }
    private ScheduledFuture<?> executeTask(AbstractTask abstractTask){
        switch (abstractTask.taskType()) {
            case TASK:{
                return addTask(abstractTask);
            }
            case DELAYED_TASK:{
                DelayedTask delayedTask = (DelayedTask)abstractTask;
                return addDelayedTask(abstractTask,delayedTask.getDelay(),delayedTask.getTimeUnit());
            }
            default:
        }
        return null;
    }
    /**
     * 添加同步任务
     * @param runnable 任务
     * @return ScheduledFuture可用于控制任务以及检查状态
     */
    private ScheduledFuture<?> addTask(AbstractTask runnable) {
        return this.scheduledThreadPoolExecutor.schedule(runnable,0,TimeUnit.NANOSECONDS);
    }

    /**
     * {@link TaskExecutorGroup#addDelayedTask(AbstractTask, long, TimeUnit)}设置延时任务默认时间单位为毫秒
     */
    private ScheduledFuture<?> addDelayedTask(AbstractTask runnable, long delay){
        return addDelayedTask(runnable,delay,TimeUnit.MILLISECONDS);
    }
    /**
     * 添加延时执行任务
     * @param runnable 任务
     * @param delay 延迟时间
     * @param timeUnit 使用的时间单位
     * @return ScheduledFuture可用于控制任务以及检查状态
     */
    private ScheduledFuture<?> addDelayedTask(AbstractTask runnable, long delay, TimeUnit timeUnit) {
        return this.scheduledThreadPoolExecutor.schedule(runnable,delay,timeUnit);
    }

    /**
     * {@link TaskExecutorGroup#addFixedRateTask(AbstractTask, long, long)}设置延时任务默认时间单位为毫秒
     */
    private ScheduledFuture<?> addFixedRateTask(AbstractTask runnable, long initalDelay, long period){
        return addFixedRateTask(runnable,initalDelay,period,TimeUnit.MILLISECONDS);
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
        return this.scheduledThreadPoolExecutor.scheduleAtFixedRate(runnable,initalDelay,period,timeUnit);
    }
}
