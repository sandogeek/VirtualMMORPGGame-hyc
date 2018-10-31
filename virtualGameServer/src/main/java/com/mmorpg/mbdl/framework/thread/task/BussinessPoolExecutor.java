package com.mmorpg.mbdl.framework.thread.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mmorpg.mbdl.framework.thread.TimeOutCaffeineMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 业务线程池，以及所有与业务线程池关联的队列
 * @author sando
 */
@Component
public class BussinessPoolExecutor {
    /** 业务线程池 */
    private ScheduledThreadPoolExecutor businessThreadPool;
    /** 业务任务队列 */
    private TimeOutCaffeineMap<Serializable, TaskQueue> businessThreadPoolTaskQueues;

    private int processors = Runtime.getRuntime().availableProcessors();
    // @Value("${server.config.thread.poolSize}")
    private int poolSize= (processors<=4)?processors*2:processors+8;
    // @Value("${server.config.taskQueue.timeout}") TODO 自带的不能注入long，自行实现新的注入方式
    private long timeout;
    @Value("${server.config.thread.name}")
    private String threadNameFommat;

    private static BussinessPoolExecutor self;
    public static BussinessPoolExecutor getIntance(){
        return self;
    }
    @PostConstruct
    private void init(){
        self = this;
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat(threadNameFommat+"%2d").build();
        this.businessThreadPool = new ScheduledThreadPoolExecutor(poolSize,namedThreadFactory);
        this.businessThreadPoolTaskQueues = new TimeOutCaffeineMap<>(timeout, TimeUnit.MINUTES,()->
                new TaskQueue(businessThreadPool)
        );
    }

    public TaskQueue getOrCreateTaskQueue(Serializable dispatcherId){
        return businessThreadPoolTaskQueues.getOrCreate(dispatcherId);
    }

    public ScheduledThreadPoolExecutor getBusinessThreadPool() {
        return businessThreadPool;
    }

    public TimeOutCaffeineMap<Serializable, TaskQueue> getBusinessThreadPoolTaskQueues() {
        return businessThreadPoolTaskQueues;
    }
}
