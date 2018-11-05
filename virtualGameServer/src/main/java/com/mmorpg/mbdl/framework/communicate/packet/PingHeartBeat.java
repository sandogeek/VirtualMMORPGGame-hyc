package com.mmorpg.mbdl.framework.communicate.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

@Component
@ProtoDesc(description = "ping心跳包")
public class PingHeartBeat extends AbstractPacket {
    @Protobuf
    private boolean placeHolder;

    @Override
    public short getPacketId() {
        return PacketIdManager.PING_HEART_BEAT;
    }
}
