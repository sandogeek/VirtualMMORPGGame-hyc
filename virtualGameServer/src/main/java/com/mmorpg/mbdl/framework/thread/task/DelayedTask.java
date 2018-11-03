package com.mmorpg.mbdl.framework.thread.task;

import java.util.concurrent.TimeUnit;

/**
 * 延迟执行的任务
 */
public abstract class DelayedTask extends AbstractTask {
    private long delay;
    private TimeUnit timeUnit;

    public DelayedTask(long delay, TimeUnit timeUnit) {
        this.delay = delay;
        this.timeUnit = timeUnit;
    }

    @Override
    public long getMaxDelay() {
        return super.getMaxDelay()+TimeUnit.NANOSECONDS.convert(delay,timeUnit);
    }

    public long getDelay() {
        return delay;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @Override
    public TaskType taskType() {
        return TaskType.DELAYED_TASK;
    }
}
