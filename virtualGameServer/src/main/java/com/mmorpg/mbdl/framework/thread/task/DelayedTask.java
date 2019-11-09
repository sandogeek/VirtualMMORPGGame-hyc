package com.mmorpg.mbdl.framework.thread.task;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 延迟执行的任务,由于任务延迟执行，所以不能放到玩家队列上，否则将导致玩家请求处理长时间延后
 */
public abstract class DelayedTask<K extends Serializable> extends AbstractTask<K> {
    private long delay;
    private TimeUnit timeUnit;

    public DelayedTask(K dispatcherId, long delay, TimeUnit timeUnit) {
        super(dispatcherId);
        this.delay = delay;
        this.timeUnit = timeUnit;
    }

    @Override
    public long getMaxDelayTime() {
        return super.getMaxDelayTime()+TimeUnit.NANOSECONDS.convert(delay,timeUnit);
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
