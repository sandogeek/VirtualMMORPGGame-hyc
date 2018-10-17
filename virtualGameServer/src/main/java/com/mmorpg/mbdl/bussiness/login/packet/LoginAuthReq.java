package com.mmorpg.mbdl.bussiness.login.packet;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.mmorpg.mbdl.bussiness.common.AbstractPacket;
import com.mmorpg.mbdl.bussiness.common.PacketId;
import org.springframework.stereotype.Component;

@Component
@ProtobufClass
public class LoginAuthReq extends AbstractPacket {
    private  String account;
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public short getPacketId() {
        return PacketId.LOGIN_AUTH_REQ;
    }
}
