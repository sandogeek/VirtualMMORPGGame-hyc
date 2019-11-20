package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 延迟执行的任务,由于任务延迟执行，所以不能放到玩家队列上，否则将导致玩家请求处理长时间延后
 */
public abstract class DelayedTask<E extends Dispatchable<T>, T extends Serializable> extends AbstractTask<E, T> {
    private long delay;
    private TimeUnit timeUnit;
    /**
     * 当前需要运行的任务
     */
    private Runnable currentRunnable;

    public DelayedTask(E dispatcher, long delay, TimeUnit timeUnit) {
        super(dispatcher, false);
        this.delay = delay;
        this.timeUnit = timeUnit;
        // 记录任务创建时间
        stopWatch.start();

        this.currentRunnable = () -> {
            getExecutor().addDelayedTask(() -> {
                TaskQueue<T> taskQueue = getTaskQueue();
                if (taskQueue == null) {
                    this.execute();
                    return;
                }
                setCurrentRunnable(this::execute);
                // 开始计时
                setCountTime(true);
                // 归队执行
                taskQueue.submit(this);
            }, delay, timeUnit);
        };
    }

    @Override
    public long getMaxDelayTime() {
        return super.getMaxDelayTime() + TimeUnit.NANOSECONDS.convert(delay,timeUnit);
    }

    @Override
    protected void beforeExecute() {
       currentRunnable.run();
    }

    public long getDelay() {
        return delay;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    private void setCurrentRunnable(Runnable currentRunnable) {
        this.currentRunnable = currentRunnable;
    }
}
