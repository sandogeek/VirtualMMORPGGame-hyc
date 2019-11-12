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
        return String.format("账号<%s>,处理请求包[%s]结束", session.getAccount(), packetMethodDifinition.getAbstractPacketClazz().getSimpleName());
    }

    @Override
    public void execute() {
        SessionState expectedState = packetMethodDifinition.getPacketMethodAnno().state();
        // 第二次状态校验，主要是为了处理未登录前同时发过来两个登录或者注册等请求的状况
        String s = packetMethodDifinition.getBean().getClass().getSimpleName() + "."
                + packetMethodDifinition.getMethod().getName() + "(...)";
        if (expectedState != SessionState.ANY) {
            if (session.getState() != expectedState) {
                getTargetLogger().warn("HandleReqTask任务执行失败，当前wsSession的状态[{}]与方法{}期待的状态[{}]不符",
                        packetMethodDifinition.getAbstractPacketClazz().getSimpleName(),
                        session.getState(), s,expectedState);
                return;
            }
        }
        Class<?> parameterType = packetMethodDifinition.getMethod().getParameterTypes()[0];
        Dispatchable user = session.getUser();
        Object obj;
        if (user != null && user.getClass() == parameterType) {
            obj = packetMethodDifinition.invoke(user, abstractPacket);
        } else if (parameterType.isAssignableFrom(session.getClass())) {
            obj = packetMethodDifinition.invoke(session, abstractPacket);
        } else if (user == null) {
            throw new RuntimeException(String.format("接受到[%s]类型的包时, session尚未绑定用户, 因此%s第一个参数类型必须extends[%s]",
                    abstractPacket.getClass().getSimpleName(), s, ISession.class.getSimpleName()
            ));
        } else {
            throw new RuntimeException("why go here");
        }

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
