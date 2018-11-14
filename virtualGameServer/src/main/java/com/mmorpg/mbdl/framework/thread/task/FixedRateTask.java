package com.mmorpg.mbdl.framework.thread.task;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public abstract class FixedRateTask extends AbstractTask {
    private long initalDelay;
    private long period;
    private TimeUnit timeUnit;

    public FixedRateTask(Serializable dispatcherId,long initalDelay, long period, TimeUnit timeUnit) {
        super(dispatcherId);
        this.initalDelay = initalDelay;
        this.period = period;
        this.timeUnit = timeUnit;
    }

    public long getInitalDelay() {
        return initalDelay;
    }

    public void setInitalDelay(long initalDelay) {
        this.initalDelay = initalDelay;
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
