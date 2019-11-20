package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.thread.PoolExecutor;
import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;

import java.io.Serializable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 固定频率执行的任务
 * @param <T> 任务分发主键的类型
 */
public abstract class FixedRateTask<E extends Dispatchable<T>, T extends Serializable> extends AbstractTask<E, T> {
    private long initDelay;
    private long period;
    private TimeUnit timeUnit;

    public FixedRateTask(E dispatcher, long initDelay, long period, TimeUnit timeUnit) {
        super(dispatcher);
        this.initDelay = initDelay;
        this.period = period;
        this.timeUnit = timeUnit;
        // 默认不打印日志
        this.setLogOrNot(false);
    }

    @Override
    protected void beforeExecute() {
        TaskQueue<T> taskQueue = getTaskQueue();
        if (taskQueue == null) {
            this.execute();
            return;
        }
        PoolExecutor<? extends Serializable, ? extends ScheduledExecutorService> poolExecutor = taskQueue.getPoolExecutor();
        final FixedRateTask<E, T> fixedRateTask = this;
        poolExecutor.addFixedRateTask(() -> {
            taskQueue.submit(new AbstractTask<E, T>(getDispatcher()) {
                @Override
                public String taskName() {
                    return "FixedRateTask转常规任务";
                }

                @Override
                public void execute() {
                    fixedRateTask.execute();
                }
            });
        }, initDelay, period, timeUnit);
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

}
