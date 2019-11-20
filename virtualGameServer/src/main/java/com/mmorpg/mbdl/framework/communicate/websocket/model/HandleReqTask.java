package com.mmorpg.mbdl.framework.communicate.websocket.model;

import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;
import com.mmorpg.mbdl.framework.thread.task.AbstractTask;

import java.io.Serializable;

/**
 * 处理请求包任务
 * @author sando
 */
public class HandleReqTask<E extends Dispatchable<T>, T extends Serializable> extends AbstractTask<E, T> {
    private PacketMethodDefinition packetMethodDefinition;
    private ISession<T> session;
    private AbstractPacket abstractPacket;
    /**
     * 使用ChannelId获取队列，玩家频繁上下线的情况下会导致产生大量无用队列，因此应使用PlayerId拿TaskQueue
     */
    public HandleReqTask(E dispatcher, PacketMethodDefinition packetMethodDefinition, ISession<T> session, AbstractPacket abstractPacket){
        super(dispatcher);
        this.setISession(session);
        this.setPacketMethodDefinition(packetMethodDefinition);
        this.setAbstractPacket(abstractPacket);
        // 根据方法注解决定是否打印日志
        this.setLogOrNot(packetMethodDefinition.getPacketMethodAnno().logOrNot());
    }

    @Override
    public String taskName() {
        return String.format("处理请求包[%s]", packetMethodDefinition.getAbstractPacketClazz().getSimpleName());
    }

    @Override
    public void execute() {
        SessionState expectedState = packetMethodDefinition.getPacketMethodAnno().state();
        // 第二次状态校验，主要是为了处理未登录前同时发过来两个登录或者注册等请求的状况
        String s = packetMethodDefinition.getBean().getClass().getSimpleName() + "."
                + packetMethodDefinition.getMethod().getName() + "(...)";
        if (expectedState != SessionState.ANY) {
            if (session.getState() != expectedState) {
                getTargetLogger().warn("HandleReqTask任务执行失败，当前wsSession的状态[{}]与方法{}期待的状态[{}]不符",
                        packetMethodDefinition.getAbstractPacketClazz().getSimpleName(),
                        session.getState(), s,expectedState);
                return;
            }
        }
        Class<?> parameterType = packetMethodDefinition.getMethod().getParameterTypes()[0];
        Dispatchable<T> user = session.getUser();
        Object obj;
        if (user != null && user.getClass() == parameterType) {
            obj = packetMethodDefinition.invoke(user, abstractPacket);
        } else if (parameterType.isAssignableFrom(session.getClass())) {
            obj = packetMethodDefinition.invoke(session, abstractPacket);
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
    public PacketMethodDefinition getPacketMethodDefinition() {
        return packetMethodDefinition;
    }

    public void setPacketMethodDefinition(PacketMethodDefinition packetMethodDefinition) {
        this.packetMethodDefinition = packetMethodDefinition;
    }

    public ISession getISession() {
        return session;
    }

    public void setISession(ISession<T> session) {
        this.session = session;
    }

    public AbstractPacket getAbstractPacket() {
        return abstractPacket;
    }

    public void setAbstractPacket(AbstractPacket abstractPacket) {
        this.abstractPacket = abstractPacket;
    }
}
