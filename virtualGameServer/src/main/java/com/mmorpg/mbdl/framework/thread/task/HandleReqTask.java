package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import com.mmorpg.mbdl.framework.communicate.websocket.model.PacketMethodDifinition;

import java.io.Serializable;

/**
 * 处理请求包任务
 * @author sando
 */
public class HandleReqTask extends Task {
    private PacketMethodDifinition packetMethodDifinition;
    private ISession session;
    private AbstractPacket abstractPacket;

    public HandleReqTask(PacketMethodDifinition packetMethodDifinition, ISession session, AbstractPacket abstractPacket){
        this.setISession(session);
        this.setPacketMethodDifinition(packetMethodDifinition);
        this.setAbstractPacket(abstractPacket);
        // 根据方法注解决定是否打印日志
        this.setLogOrNot(packetMethodDifinition.getPacketMethodAnno().logOrNot());
    }

    @Override
    public Serializable getDispatcherId() {
        return session.getId();
    }

    @Override
    public String taskName() {
        String name = String.format("处理请求包[%s]",packetMethodDifinition.getAbstractPacketClazz().getSimpleName());
        return name;
    }

    @Override
    public void execute() {
        Object obj = packetMethodDifinition.invoke(session,abstractPacket);
        if (obj != null){
            session.sendPacket((AbstractPacket) obj);
        }
    }
    public PacketMethodDifinition getPacketMethodDifinition() {
        return packetMethodDifinition;
    }

    public void setPacketMethodDifinition(PacketMethodDifinition packetMethodDifinition) {
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
