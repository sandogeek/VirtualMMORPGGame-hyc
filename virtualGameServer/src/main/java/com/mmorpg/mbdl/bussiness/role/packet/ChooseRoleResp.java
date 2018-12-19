package com.mmorpg.mbdl.bussiness.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;
/**
 * 选择角色响应
 *
 * @author Sando Geek
 * @since v1.0 2018/12/19
 **/
@Component
@ProtoDesc(description = "选择角色响应")
public class ChooseRoleResp extends AbstractPacket {
    @Protobuf(description = "结果",required = true)
    private boolean result;

    public ChooseRoleResp setResult(boolean result) {
        this.result = result;
        return this;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.CHOOSE_ROLE_RESP;
    }
}
