package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.thread.PoolExecutor;
import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 任务队列<br>
 * 玩家的任务通常串行执行，所以每个玩家一个队列,如果模块串行执行，也可以给模块一个队列
 * @author sando
 * @param <T> 任务队列的唯一标识的类型
 */
public class TaskQueue<T extends Serializable> {
    /**
     * 此任务队列的唯一标识
     */
    private T key;
    /**
     * 使用的业务线程池
     */
    private final PoolExecutor<T, ? extends ScheduledExecutorService> poolExecutor;
    /**
     * 存放任务的队列
     */
    private final Queue<AbstractTask<? extends Dispatchable<T>, T>> queue;
    /**
     * 队列是否有任务在线程池中
     */
    private AtomicBoolean hasTaskInPool = new AtomicBoolean(false);

    public TaskQueue(T key, PoolExecutor<T, ? extends ScheduledExecutorService> poolExecutor) {
        this.key = key;
        this.poolExecutor = poolExecutor;
        this.queue = new LinkedList<>();
    }

    /**
     * 往任务队列提交一个需要串行执行的任务(加到队尾)
     * TODO 返回CustomScheduledFuture来避免串行任务ScheduledFuture丢失的问题，CustomScheduledFuture内部包含一个
     * 真正的ScheduledFuture，任务加到线程池时赋值（所以任务内部需要有字段CustomScheduledFuture，在
     * {@link TaskQueue#andThen}
     * 获取并给真正的ScheduledFuture赋值）
     * @return
     */
    public ScheduledFuture<?> submit(AbstractTask<? extends Dispatchable<T>, T> abstractTask){
        abstractTask.setTaskQueue(this);
        if (hasTaskInPool.compareAndSet(false, true)) {
            return poolExecutor.executeTask(abstractTask);
        } else {
            synchronized (queue) {
                this.queue.offer(abstractTask);
            }
        }
        return null;
    }

    /**
     * 执行完一个任务后的处理
     * @return
     */
    public void andThen() {
        synchronized (queue) {
            AbstractTask<? extends Dispatchable<T>, T> abstractTask = queue.poll();
            if (abstractTask != null) {
                poolExecutor.executeTask(abstractTask);
            } else {
                hasTaskInPool.set(false);
            }
        }
    }

    public T getKey() {
        return key;
    }

    public PoolExecutor<T, ? extends ScheduledExecutorService> getPoolExecutor() {
        return poolExecutor;
    }
}
