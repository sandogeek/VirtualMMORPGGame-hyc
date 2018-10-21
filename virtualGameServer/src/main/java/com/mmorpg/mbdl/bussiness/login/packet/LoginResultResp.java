package com.mmorpg.mbdl.bussiness.login.packet;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.bussiness.login.model.LoninResultType;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

@Component
@ProtobufClass
public class LoginResultResp extends AbstractPacket {
    private String resultType;

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.LOGIN_RESULT_RESP;
    }
}
