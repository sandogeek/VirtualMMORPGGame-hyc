package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.communicate.websocket.model.PacketMethodDifinition;
import com.mmorpg.mbdl.framework.communicate.websocket.model.SessionState;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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

    public void dispatch(AbstractTask abstractTask){
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
}
