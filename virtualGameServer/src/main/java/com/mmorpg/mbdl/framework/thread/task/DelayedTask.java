package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        super(dispatcher);
        this.delay = delay;
        this.timeUnit = timeUnit;
        // 存储原设置
        boolean logOrNot = isLogOrNot();
        // 不打日志，不暴露这里的处理
        setLogOrNot(true);
        Logger logger = LoggerFactory.getLogger(this.getClass());
        this.currentRunnable = () -> {
            logger.error("开始添加延时任务");
            getExecutor().addDelayedTask(() -> {
                logger.error("延时完毕");
                TaskQueue<T> taskQueue = getTaskQueue();
                if (taskQueue == null) {
                    this.execute();
                    return;
                }
                // 恢复原来的日志设置
                setLogOrNot(logOrNot);
                Runnable execute = this::execute;
                setCurrentRunnable(execute);
                // 归队执行
                taskQueue.submit(this);
            }, delay, timeUnit);
        };
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
