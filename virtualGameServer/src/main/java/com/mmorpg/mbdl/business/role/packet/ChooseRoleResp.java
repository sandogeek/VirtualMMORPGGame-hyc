package com.mmorpg.mbdl.business.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.business.object.packet.CustomRoleUiInfoResp;
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
    @Protobuf(description = "玩家自身详细信息")
    private CustomRoleUiInfoResp customRoleUiInfoResp;

    public ChooseRoleResp setCustomRoleUiInfoResp(CustomRoleUiInfoResp customRoleUiInfoResp) {
        this.customRoleUiInfoResp = customRoleUiInfoResp;
        return this;
    }

    public ChooseRoleResp setResult(boolean result) {
        this.result = result;
        return this;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.CHOOSE_ROLE_RESP;
    }
}
