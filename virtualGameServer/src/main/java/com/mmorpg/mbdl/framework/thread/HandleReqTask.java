package com.mmorpg.mbdl.framework.thread;

import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import com.mmorpg.mbdl.framework.communicate.websocket.model.PacketMethodDifinition;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WsSession;

/**
 * 处理请求包任务
 * @author sando
 */
public class HandleReqTask extends AbstractTask {
    private PacketMethodDifinition packetMethodDifinition;
    private WsSession wsSession;
    private AbstractPacket abstractPacket;

    public HandleReqTask(PacketMethodDifinition packetMethodDifinition,WsSession wsSession,AbstractPacket abstractPacket){
        this.setWsSession(wsSession);
        this.setPacketMethodDifinition(packetMethodDifinition);
        this.setAbstractPacket(abstractPacket);
    }

    @Override
    protected boolean logOrNot() {
        return packetMethodDifinition.getPacketMethodAnno().logOrNot();
    }

    @Override
    public int getDispatcherId() {
        if (wsSession.getPlayerId() == null){
            return wsSession.hashCode();
        }
        return wsSession.getPlayerId().intValue();
    }

    @Override
    public String taskName() {
        String name = String.format("处理请求包[class=%s]",packetMethodDifinition.getaClazz().getSimpleName());
        return name;
    }

    @Override
    public void execute() {
        Object obj = packetMethodDifinition.invoke(wsSession,abstractPacket);
        if (obj != null){
            wsSession.sendPacket((AbstractPacket) obj);
        }
    }
    public PacketMethodDifinition getPacketMethodDifinition() {
        return packetMethodDifinition;
    }

    public void setPacketMethodDifinition(PacketMethodDifinition packetMethodDifinition) {
        this.packetMethodDifinition = packetMethodDifinition;
    }

    public WsSession getWsSession() {
        return wsSession;
    }

    public void setWsSession(WsSession wsSession) {
        this.wsSession = wsSession;
    }

    public AbstractPacket getAbstractPacket() {
        return abstractPacket;
    }

    public void setAbstractPacket(AbstractPacket abstractPacket) {
        this.abstractPacket = abstractPacket;
    }
}
