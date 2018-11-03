package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.communicate.websocket.model.PacketMethodDifinition;
import com.mmorpg.mbdl.framework.communicate.websocket.model.SessionState;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WsSession;
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
        if (intoThreadPoolDirectly){
            return BussinessPoolExecutor.getIntance().executeTask(abstractTask);
        }
        TaskQueue taskQueue = bussinessPoolExecutor.getBusinessThreadPoolTaskQueues().getOrCreate(abstractTask.getDispatcherId());
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
                    logger.warn("HandleReqTask({})分发失败，当前wsSession的状态[{}]与方法期待的状态[{}]不符",
                            packetMethodDifinition.getAbstractPacketClazz().getSimpleName(),wsSession.getState(),expectedState);
                    return null;
                }
            }
            if (executeParallel){
                return BussinessPoolExecutor.getIntance().executeTask(abstractTask);
            }
        }
        return taskQueue.submit(abstractTask);
    }

    /**
     * 分发任务，但不是直接分发到线程池
     * 如果是HandleReqTask，根据@PacketMethod决定分发到队列还是分发到线程池
     * 如果是其它任务，则
     * @param abstractTask
     */
    public ScheduledFuture<?> dispatch(AbstractTask abstractTask){
       return dispatch(abstractTask,false);
    }
}
