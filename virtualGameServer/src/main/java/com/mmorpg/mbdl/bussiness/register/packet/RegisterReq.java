package com.mmorpg.mbdl.bussiness.register.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

@Component
@ProtoDesc(description = "注册请求")
public class RegisterReq extends AbstractPacket {
    @Protobuf
    private String account;
    @Protobuf
    private String password;
    @Override
    public short getPacketId() {
        return PacketIdManager.REGISTER_REQ;
    }
}
