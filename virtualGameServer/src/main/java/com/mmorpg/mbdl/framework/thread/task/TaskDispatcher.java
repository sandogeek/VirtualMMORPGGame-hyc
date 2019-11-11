package com.mmorpg.mbdl.framework.thread.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mmorpg.mbdl.framework.communicate.websocket.model.HandleReqTask;
import com.mmorpg.mbdl.framework.thread.BusinessPoolExecutor;
import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
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
    private BusinessPoolExecutor<Serializable, EventExecutorGroup> businessPoolExecutor;

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
        businessPoolExecutor = new BusinessPoolExecutor<>(eventExecutors, 1, TimeUnit.MINUTES);
    }

    /**
     * 分发任务
     * @param abstractTask 抽象任务
     * @param intoThreadPoolDirectly 是否直接分发到线程池，而不是加到队列
     * @return 如果任务分发成功并被提交到线程池，返回ScheduledFuture，否则抛出异常
     */
    public ScheduledFuture<?> dispatch(AbstractTask<Dispatchable<Serializable>> abstractTask, boolean intoThreadPoolDirectly){
        if (abstractTask == null){
            throw new IllegalArgumentException("分发了一个空任务");
        }
        if (intoThreadPoolDirectly){
            abstractTask.setExecuteParallel(true);
            return (ScheduledFuture<?>) businessPoolExecutor.executeTask(abstractTask);
        }
        // dispatcherId为null的任务并行执行
        if (abstractTask.getDispatcher()==null){
            abstractTask.setExecuteParallel(true);
            return (ScheduledFuture<?>)businessPoolExecutor.executeTask(abstractTask);
        }
        // 如果不是请求处理任务，校验其是否占用了未登录时使用的队列
        if (!(abstractTask instanceof HandleReqTask)){
            Serializable dispatcherId = abstractTask.getDispatcher().dispatchId();
            if (dispatcherId instanceof Long){
                if ((Long)dispatcherId < 0){
                    throw new IllegalArgumentException("任务分发失败，dispatcherId小于0，dispatcherId小于0的队列预留给未登录前的请求使用");
                }
            }
        }
        TaskQueue<Serializable> taskQueue = businessPoolExecutor.getOrCreateTaskQueue(abstractTask.getDispatcher().dispatchId());
        abstractTask.setTaskQueue(taskQueue);
        return (ScheduledFuture<?>)taskQueue.submit(abstractTask);
    }

    /**
     * 分发任务，但不是直接分发到线程池
     * 如果是HandleReqTask，根据@PacketMethod决定分发到队列还是分发到线程池
     * @param abstractTask
     */
    public ScheduledFuture<?> dispatch(AbstractTask<Dispatchable<Serializable>> abstractTask){
       return dispatch(abstractTask,false);
    }
}
