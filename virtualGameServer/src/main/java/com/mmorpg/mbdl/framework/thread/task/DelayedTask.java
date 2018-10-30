package com.mmorpg.mbdl.framework.thread.task;

import java.util.concurrent.TimeUnit;

/**
 * 延迟执行的任务
 */
public abstract class DelayedTask extends AbstractTask {
    private long delay;
    private TimeUnit timeUnit;

    public DelayedTask(TaskQueue taskQueue,long delay, TimeUnit timeUnit) {
        this.delay = delay;
        this.timeUnit = timeUnit;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    @Override
    public TaskType taskType() {
        return TaskType.DELAYED_TASK;
    }
}
