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
    private boolean logOrNotOrigin;
    /**
     * 当前需要运行的任务
     */
    private Runnable currentRunnable;

    public DelayedTask(E dispatcher, long delay, TimeUnit timeUnit) {
        super(dispatcher);
        this.delay = delay;
        this.timeUnit = timeUnit;
        logOrNotOrigin = isLogOrNot();
        // 不打日志，不暴露这里的处理
        setLogOrNot(false);
    }

    @Override
    protected void beforeExecute() {
        DelayedTask<E, T> delayedTask = this;
        getExecutor().addDelayedTask(() -> {
            TaskQueue<T> taskQueue = getTaskQueue();
            if (taskQueue == null) {
                this.execute();
                return;
            }
            // 归队执行
            taskQueue.submit(new AbstractTask<Dispatchable<T>, T>(delayedTask.getDispatcher()) {
                @Override
                public String taskName() {
                    return delayedTask.taskName();
                }

                @Override
                public void execute() {
                    delayedTask.execute();
                }
            }.setLogOrNot(logOrNotOrigin));
        }, delay, timeUnit);
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
