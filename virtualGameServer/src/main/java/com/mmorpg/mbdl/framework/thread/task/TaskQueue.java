package com.mmorpg.mbdl.framework.thread.task;

import com.google.common.collect.Lists;
import com.mmorpg.mbdl.framework.thread.BusinessPoolExecutor;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.Queue;
import java.util.concurrent.ScheduledFuture;

/**
 * 任务队列<br>
 * 玩家的任务通常串行执行，所以每个玩家一个队列,如果模块串行执行，也可以给模块一个队列
 * @author sando
 */
public class TaskQueue {
    /**
     * 与此任务队列相关的线程池
     */
    private final EventExecutorGroup executors;
    /**
     * 存放任务的队列
     */
    private Queue<AbstractTask> queue;

    public TaskQueue(EventExecutorGroup executors) {
        this.executors = executors;
        this.queue = Lists.newLinkedList();
    }

    /**
     * 往任务队列提交一个需要串行执行的任务(加到队尾)
     * TODO 返回CustomScheduledFuture来避免串行任务ScheduledFuture丢失的问题，CustomScheduledFuture内部包含一个
     * 真正的ScheduledFuture，任务加到线程池时赋值（所以任务内部需要有字段CustomScheduledFuture，在{@link TaskQueue#andThen}
     * 获取并给真正的ScheduledFuture赋值）
     * @return
     */
    public ScheduledFuture<?> submit(AbstractTask abstractTask){
        synchronized (this){
            this.queue.add(abstractTask);
            if (queue.size()==1){
                // 只有一个任务的话，说明是刚加的，立即送到线程池里的队列执行
                return BusinessPoolExecutor.getInstance().executeTask(abstractTask);
            }
        }
        return null;
    }

    /**
     * 任务队列中可以并行执行的任务，直接交给线程池，而不是加到队尾
     * @return
     */
    public ScheduledFuture<?> executeParallel(AbstractTask abstractTask){
        return BusinessPoolExecutor.getInstance().executeTask(abstractTask);
    }
    /**
     * 执行完一个任务后的处理
     * @return
     */
    public ScheduledFuture<?> andThen() {
        synchronized (this){
            // 移除执行完毕的任务
            queue.poll();
            if (!queue.isEmpty()) {
                // 有任务继续执行
                return BusinessPoolExecutor.getInstance().executeTask(queue.peek());
            }
        }
        return null;
    }
}
