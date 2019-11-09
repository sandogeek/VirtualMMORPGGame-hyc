package com.mmorpg.mbdl.framework.thread.task;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 固定频率执行的任务
 * @param <K> 任务分发主键的类型
 */
public abstract class FixedRateTask<K extends Serializable> extends AbstractTask<K> {
    private long initalDelay;
    private long period;
    private TimeUnit timeUnit;

    public FixedRateTask(K dispatcherId, long initDelay, long period, TimeUnit timeUnit) {
        super(dispatcherId);
        this.initalDelay = initDelay;
        this.period = period;
        this.timeUnit = timeUnit;
        // 默认不打印日志
        this.setLogOrNot(false);
    }

    public long getInitalDelay() {
        return initalDelay;
    }

    public void setInitalDelay(long initDelay) {
        this.initalDelay = initDelay;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    @Override
    public TaskType taskType() {
        return null;
    }

}
