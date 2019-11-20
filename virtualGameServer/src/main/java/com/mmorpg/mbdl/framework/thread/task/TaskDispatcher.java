package com.mmorpg.mbdl.framework.thread.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mmorpg.mbdl.framework.thread.PoolExecutor;
import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 任务分发器
 * @author sando
 */
@Component
public class TaskDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(TaskDispatcher.class);
    @Value("${server.config.thread.name}")
    private String threadNameFormat;
    @Value("${server.config.thread.poolSize}")
    private int poolSize;
    private PoolExecutor<Long, EventExecutorGroup> businessPoolExecutor;

    private static TaskDispatcher self;
    public static TaskDispatcher getInstance(){
        return self;
    }

    @PostConstruct
    private void init(){
        self = this;
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(threadNameFormat +"%2d").build();
        if (poolSize == 0) {
            int processors = Runtime.getRuntime().availableProcessors();
            poolSize = (processors <= 4) ? processors * 2 : processors + 8;
        }
        DefaultEventExecutorGroup eventExecutors = new DefaultEventExecutorGroup(poolSize, namedThreadFactory);
        businessPoolExecutor = new PoolExecutor<>(eventExecutors, 1, TimeUnit.MINUTES);
    }

    /**
     * 分发任务
     * @param abstractTask 抽象任务
     * @param intoThreadPoolDirectly 是否直接分发到线程池，而不是加到队列
     * @return 如果任务分发成功并被提交到线程池，返回ScheduledFuture，否则抛出异常
     */
    public <E extends Dispatchable<Long>> ScheduledFuture<?> dispatch(AbstractTask<E, Long> abstractTask, boolean intoThreadPoolDirectly){
        if (abstractTask == null){
            throw new IllegalArgumentException("分发了一个空任务");
        }
        abstractTask.setExecutor(businessPoolExecutor);
        if (intoThreadPoolDirectly){
            abstractTask.setExecuteParallel(true);
            return (ScheduledFuture<?>) businessPoolExecutor.executeTask(abstractTask);
        }
        // dispatcherId为null的任务并行执行
        if (abstractTask.getDispatcher()==null){
            abstractTask.setExecuteParallel(true);
            return (ScheduledFuture<?>) businessPoolExecutor.executeTask(abstractTask);
        }

        TaskQueue<Long> taskQueue = businessPoolExecutor.getOrCreateTaskQueue(abstractTask.getDispatcher().dispatchId());
        return (ScheduledFuture<?>)taskQueue.submit(abstractTask);
    }

    /**
     * 分发任务，但不是直接分发到线程池
     * 如果是HandleReqTask，根据@PacketMethod决定分发到队列还是分发到线程池
     * @param abstractTask
     */
    public <E extends Dispatchable<Long>> ScheduledFuture<?> dispatch(AbstractTask<E, Long> abstractTask){
       return dispatch(abstractTask,false);
    }
}
