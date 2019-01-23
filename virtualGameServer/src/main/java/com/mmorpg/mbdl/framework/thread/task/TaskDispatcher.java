package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.communicate.websocket.model.HandleReqTask;
import com.mmorpg.mbdl.framework.thread.BusinessPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.concurrent.ScheduledFuture;

/**
 * 任务分发器
 * @author sando
 */
@Component
public class TaskDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(TaskDispatcher.class);
    private static TaskDispatcher self;
    public static TaskDispatcher getInstance(){
        return self;
    }
    @PostConstruct
    private void init(){
        self = this;
    }
    @Autowired
    private BusinessPoolExecutor businessPoolExecutor;

    /**
     * 分发任务
     * @param abstractTask 抽象任务
     * @param intoThreadPoolDirectly 是否直接分发到线程池，而不是加到队列
     * @return 如果任务分发成功并被提交到线程池，返回ScheduledFuture，否则返回null
     */
    public ScheduledFuture<?> dispatch(AbstractTask abstractTask, boolean intoThreadPoolDirectly){
        if (abstractTask == null){
            return null;
        }
        if (intoThreadPoolDirectly){
            abstractTask.setExecuteParallel(true);
            return BusinessPoolExecutor.getInstance().executeTask(abstractTask);
        }
        // dispatcherId为null的任务并行执行
        if (abstractTask.getDispatcherId()==null){
            abstractTask.setExecuteParallel(true);
            return BusinessPoolExecutor.getInstance().executeTask(abstractTask);
        }
        // 如果不是请求处理任务，校验其是否占用了未登录时使用的队列
        if (!(abstractTask instanceof HandleReqTask)){
            Serializable dispatcherId = abstractTask.getDispatcherId();
            if (dispatcherId instanceof Long){
                if ((Long)dispatcherId < 0){
                    throw new IllegalArgumentException(String.format("任务分发失败，dispatcherId小于0，dispatcherId小于0的队列预留给未登录前的请求使用"));
                }
            }
        }
        TaskQueue taskQueue = businessPoolExecutor.getBusinessThreadPoolTaskQueues().getOrCreate(abstractTask.getDispatcherId());
        return taskQueue.submit(abstractTask);
    }

    /**
     * 分发任务，但不是直接分发到线程池
     * 如果是HandleReqTask，根据@PacketMethod决定分发到队列还是分发到线程池
     * @param abstractTask
     */
    public ScheduledFuture<?> dispatch(AbstractTask abstractTask){
       return dispatch(abstractTask,false);
    }
}
