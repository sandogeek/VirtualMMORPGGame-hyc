package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import com.mmorpg.mbdl.framework.communicate.websocket.model.PacketMethodDifinition;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WsSession;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 处理请求包任务
 * @author sando
 */
public class HandleReqTask extends Task {
    private PacketMethodDifinition packetMethodDifinition;
    private WsSession wsSession;
    private AbstractPacket abstractPacket;

    public HandleReqTask(PacketMethodDifinition packetMethodDifinition,WsSession wsSession,AbstractPacket abstractPacket){
        this.setWsSession(wsSession);
        this.setPacketMethodDifinition(packetMethodDifinition);
        this.setAbstractPacket(abstractPacket);
        super.setMaxDelay(TimeUnit.NANOSECONDS.convert(2,TimeUnit.MILLISECONDS));
        super.setMaxExecute(TimeUnit.NANOSECONDS.convert(3,TimeUnit.MILLISECONDS));
    }

    @Override
    public Serializable getDispatcher() {
        return wsSession.getId();
    }

    @Override
    public String taskName() {
        String name = String.format("处理请求包[%s]",packetMethodDifinition.getAbstractPacketClazz().getSimpleName());
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
