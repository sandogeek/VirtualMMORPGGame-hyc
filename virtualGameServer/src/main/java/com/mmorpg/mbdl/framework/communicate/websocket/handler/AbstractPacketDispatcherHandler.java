package com.mmorpg.mbdl.framework.communicate.websocket.handler;

import com.mmorpg.mbdl.framework.communicate.websocket.model.*;
import com.mmorpg.mbdl.framework.communicate.websocket.model.HandleReqTask;
import com.mmorpg.mbdl.framework.thread.task.TaskDispatcher;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * AbstractPacket包分发处理器，用于将AbstractPacket对象赋值给相应模块对应的处理方法的形参
 * (因为是Inbound所以只有请求包能到达这里)
 * 所以需要一个 Table <abstractPacket.getClass,method, bean（Object>
 * Reflections库以及spring的ReflectionUtils可以帮助完成以上需求
 * @author sando
 */
@ChannelHandler.Sharable
@Component
public class AbstractPacketDispatcherHandler extends SimpleChannelInboundHandler<AbstractPacket> {
    private static final Logger logger= LoggerFactory.getLogger(AbstractPacketDispatcherHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractPacket abstractPacket) throws Exception {
        // 不能在netty worker线程池作业务处理,如果当前请求处理发生阻塞，那么这条（4条之一）worker线程就会被阻塞
        PacketMethodDifinition packetMethodDifinition = PacketMethodDifinitionManager.getIntance().getPacketMethodDifinition(abstractPacket);
        if (packetMethodDifinition==null) {
            logger.error("请求包[{}]没有对应的@PacketMethod方法处理",abstractPacket.getClass().getSimpleName());
            return;
        }
        SessionState expectedState = packetMethodDifinition.getPacketMethodAnno().state();
        ISession session = SessionManager.getIntance().getSession(ctx.channel().id());
        boolean executeParallel = packetMethodDifinition.getPacketMethodAnno().executeParallel();
        // 状态校验，不符合要求的请求直接不生成任务
        if (expectedState!=SessionState.ANY){
            if (session.getState() != expectedState){
                logger.warn("HandleReqTask[{}]分发失败，当前wsSession的状态[{}]与方法{}期待的状态[{}]不符",
                        packetMethodDifinition.getAbstractPacketClazz().getSimpleName(),
                        session.getState(),packetMethodDifinition.getBean().getClass().getSimpleName()+"."
                                +packetMethodDifinition.getMethod().getName()+"(...)",expectedState);
                return;
            }
        }
        TaskDispatcher.getInstance().dispatch(
                new HandleReqTask(session.selectDispatcherId(),packetMethodDifinition, session,abstractPacket)
                        .setMaxExecuteTime(TimeUnit.NANOSECONDS.convert(30,TimeUnit.MILLISECONDS)),executeParallel);
        // TaskExecutorGroup.addTask(new HandleReqTask(packetMethodDifinition,session,abstractPacket));
    }

}
