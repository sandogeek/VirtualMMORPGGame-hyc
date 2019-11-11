package com.mmorpg.mbdl.framework.communicate.websocket.model;

import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;
import com.mmorpg.mbdl.framework.thread.task.BaseNormalTask;

import java.io.Serializable;

/**
 * 处理请求包任务
 * @author sando
 */
public class HandleReqTask<K extends Dispatchable<? extends Serializable>> extends BaseNormalTask<K> {
    private PacketMethodDifinition packetMethodDifinition;
    private ISession session;
    private AbstractPacket abstractPacket;
    // 使用ChannelId获取队列，玩家频繁上下线的情况下会导致产生大量无用队列，因此应使用PlayerId拿TaskQueue
    public HandleReqTask(K dispatcher,PacketMethodDifinition packetMethodDifinition, ISession session, AbstractPacket abstractPacket){
        super(dispatcher);
        this.setISession(session);
        this.setPacketMethodDefinition(packetMethodDifinition);
        this.setAbstractPacket(abstractPacket);
        // 根据方法注解决定是否打印日志
        this.setLogOrNot(packetMethodDifinition.getPacketMethodAnno().logOrNot());
    }

    @Override
    public String taskName() {
        String name = String.format("账号<%s>,处理请求包[%s]结束", session.getAccount(),packetMethodDifinition.getAbstractPacketClazz().getSimpleName());
        return name;
    }

    @Override
    public void execute() {
        SessionState expectedState = packetMethodDifinition.getPacketMethodAnno().state();
        // 第二次状态校验，主要是为了处理未登录前同时发过来两个登录或者注册等请求的状况
        if (expectedState != SessionState.ANY) {
            if (session.getState() != expectedState) {
                getTargetLogger().warn("HandleReqTask任务执行失败，当前wsSession的状态[{}]与方法{}期待的状态[{}]不符",
                        packetMethodDifinition.getAbstractPacketClazz().getSimpleName(),
                        session.getState(),packetMethodDifinition.getBean().getClass().getSimpleName()+"."
                                +packetMethodDifinition.getMethod().getName()+"(...)",expectedState);
                return;
            }
        }

        Object obj = packetMethodDifinition.invoke(session,abstractPacket);
        if (obj != null){
            session.sendPacket((AbstractPacket) obj);
        }
    }
    public PacketMethodDifinition getPacketMethodDifinition() {
        return packetMethodDifinition;
    }

    public void setPacketMethodDefinition(PacketMethodDifinition packetMethodDifinition) {
        this.packetMethodDifinition = packetMethodDifinition;
    }

    public ISession getISession() {
        return session;
    }

    public void setISession(ISession session) {
        this.session = session;
    }

    public AbstractPacket getAbstractPacket() {
        return abstractPacket;
    }

    public void setAbstractPacket(AbstractPacket abstractPacket) {
        this.abstractPacket = abstractPacket;
    }
}
