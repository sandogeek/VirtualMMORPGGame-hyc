package com.mmorpg.mbdl.framework.thread.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mmorpg.mbdl.framework.communicate.websocket.model.PacketMethodDifinition;
import com.mmorpg.mbdl.framework.communicate.websocket.model.SessionState;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WsSession;
import com.mmorpg.mbdl.framework.thread.TimeOutCaffeineMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 任务分发器
 * @author sando
 */
@Component
public class TaskDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(TaskDispatcher.class);
    private int processors = Runtime.getRuntime().availableProcessors();
    // @Value("${server.config.thread.poolSize}")
    private int poolSize= (processors<=4)?processors*2:processors+8;
    // @Value("${server.config.taskQueue.timeout}") TODO 自带的不能注入long，自行实现新的注入方式
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
        /**
         * 状态校验,是否并行处理
         */
        if (abstractTask instanceof HandleReqTask){
            HandleReqTask handleReqTask = (HandleReqTask)abstractTask;
            PacketMethodDifinition packetMethodDifinition = handleReqTask.getPacketMethodDifinition();
            WsSession wsSession = handleReqTask.getWsSession();
            SessionState expectedState = packetMethodDifinition.getPacketMethodAnno().state();
            boolean executeParallel = packetMethodDifinition.getPacketMethodAnno().executeParallel();
            if (expectedState!=SessionState.ANY){
                if (wsSession.getState() != expectedState){
                    logger.warn("请求任务{}分发失败，当前wsSession的状态[{}]与方法期待的状态[{}]不符",
                            packetMethodDifinition.getAbstractPacketClazz().getSimpleName(),wsSession.getState(),expectedState);
                    return;
                }
            }
            if (executeParallel){
                taskQueue.executeParallel(abstractTask);
                return;
            }
        }
       taskQueue.submit(abstractTask);
    }

    public TaskQueue getOrCreateTaskQueue(Long dispatcherId){
        return businessThreadPoolTaskQueue.getOrCreate(dispatcherId);
    }

}
