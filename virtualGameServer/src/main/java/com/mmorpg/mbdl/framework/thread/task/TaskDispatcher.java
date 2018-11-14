package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.thread.BussinessPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
        if (intoThreadPoolDirectly||abstractTask.getDispatcherId()==null){
            return BussinessPoolExecutor.getIntance().executeTask(abstractTask);
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
