package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.communicate.websocket.model.HandleReqTask;
import com.mmorpg.mbdl.framework.thread.BussinessPoolExecutor;
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
    public static TaskDispatcher getIntance(){
        return self;
    }
    @PostConstruct
    private void init(){
        self = this;
    }
    @Autowired
    private BussinessPoolExecutor bussinessPoolExecutor;

    /**
     * 分发任务
     * @param abstractTask 抽象任务
     * @param intoThreadPoolDirectly 是否直接分发到线程池，而不是加到队列
     * @return 如果任务分发成功并被提交到线程池，返回ScheduledFuture，否则返回null
     */
    public ScheduledFuture<?> dispatch(AbstractTask abstractTask, boolean intoThreadPoolDirectly){
        if (intoThreadPoolDirectly){
            abstractTask.setExecuteParallel(true);
            // if (abstractTask.getDispatcherId()!=null){
            //     /** 设置并行任务的dispatcherId为null,否则这个任务执行到最后会执行{@link TaskQueue#andThen()}}方法，导致原队列的后续任务并行 */
            //     abstractTask.setDispatcherId(null);
            // }
            return BussinessPoolExecutor.getIntance().executeTask(abstractTask);
        }
        // dispatcherId为null的任务并行执行
        if (abstractTask.getDispatcherId()==null){
            abstractTask.setExecuteParallel(true);
            return BussinessPoolExecutor.getIntance().executeTask(abstractTask);
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
        TaskQueue taskQueue = bussinessPoolExecutor.getBusinessThreadPoolTaskQueues().getOrCreate(abstractTask.getDispatcherId());
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
