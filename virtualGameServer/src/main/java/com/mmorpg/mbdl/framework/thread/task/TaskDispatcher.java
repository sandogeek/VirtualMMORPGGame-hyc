package com.mmorpg.mbdl.framework.thread.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mmorpg.mbdl.framework.thread.TimeOutCaffeineMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 任务分发器
 * @author sando
 */
@Component
public class TaskDispatcher {
    private int processors = Runtime.getRuntime().availableProcessors();
    // @Value("${server.config.thread.poolSize}")
    private int poolSize= (processors<=4)?processors*2:processors+8;
    // @Value("${server.config.taskQueue.timeout}")
    private long timeout;
    @Value("${server.config.thread.name}")
    private String threadNameFommat;

    private static TaskDispatcher self;
    public static TaskDispatcher getIntance(){
        return self;
    }

    /** 业务线程池... */
    private ScheduledThreadPoolExecutor businessThreadPool;
    /** 业务任务队列 */
    private TimeOutCaffeineMap<Long, TaskQueue> businessThreadPoolTaskQueue;

    @PostConstruct
    private void init(){
        self = this;
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(threadNameFommat+"%2d").build();
        this.businessThreadPool = new ScheduledThreadPoolExecutor(poolSize,namedThreadFactory);
        this.businessThreadPoolTaskQueue = new TimeOutCaffeineMap<>(timeout, TimeUnit.MINUTES,()->
            new TaskQueue(businessThreadPool)
        );
    }

    public void dispatch(AbstractTask abstractTask){
       TaskQueue taskQueue = businessThreadPoolTaskQueue.getOrCreate(abstractTask.getDispatcherId());
       taskQueue.submit(abstractTask);
    }

    public TaskQueue getOrCreateTaskQueue(Long dispatcherId){
        return businessThreadPoolTaskQueue.getOrCreate(dispatcherId);
    }

}
