package com.mmorpg.mbdl.business.register.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

@Component
@ProtoDesc(description = "注册请求")
public class RegisterReq extends AbstractPacket {
    @Protobuf(required = true)
    private String account;
    @Protobuf(required = true)
    private String password;
    @Override
    public short getPacketId() {
        return PacketIdManager.REGISTER_REQ;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}
