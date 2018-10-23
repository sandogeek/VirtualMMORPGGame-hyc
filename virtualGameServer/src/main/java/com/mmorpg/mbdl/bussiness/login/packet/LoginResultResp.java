package com.mmorpg.mbdl.bussiness.login.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.bussiness.login.model.LoninResultType;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

/**
 * 登录结果响应包
 */
@Component
@ProtoDesc(description = "登录结果响应包")
public class LoginResultResp extends AbstractPacket {
    @Protobuf(description = "登录结果",required = true)
    private LoninResultType resultType;

    public LoninResultType getResultType() {
        return resultType;
    }

    public void setResultType(LoninResultType resultType) {
        this.resultType = resultType;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.LOGIN_RESULT_RESP;
    }
}
