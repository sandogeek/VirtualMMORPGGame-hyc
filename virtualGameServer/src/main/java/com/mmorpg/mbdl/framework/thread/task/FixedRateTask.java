package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 固定频率执行的任务
 * @param <K> 任务分发主键的类型
 */
public abstract class FixedRateTask<K extends Dispatchable<? extends Serializable>> extends AbstractTask<K> {
    private long initDelay;
    private long period;
    private TimeUnit timeUnit;

    public FixedRateTask(K dispatcher, long initDelay, long period, TimeUnit timeUnit) {
        super(dispatcher);
        this.initDelay = initDelay;
        this.period = period;
        this.timeUnit = timeUnit;
        // 默认不打印日志
        this.setLogOrNot(false);
    }

    public long getInitDelay() {
        return initDelay;
    }

    public void setInitDelay(long initDelay) {
        this.initDelay = initDelay;
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
