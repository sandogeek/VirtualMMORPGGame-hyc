package com.mmorpg.mbdl.business.register.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

/**
 * 注册响应
 *
 * @author Sando Geek
 * @since v1.0 2018/12/10
 **/
@Component
@ProtoDesc(description = "注册响应包")
public class RegisterResp extends AbstractPacket {
    @Protobuf(description = "成功与否",required = true)
    private boolean isSuccess;

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.REGISTER_RESP;
    }
}
