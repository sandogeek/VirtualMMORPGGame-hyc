package com.mmorpg.mbdl.business.role.packet;

/**
 * 添加角色响应
 *
 * @author Sando Geek
 * @since v1.0 2018/12/17
 **/

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.business.role.packet.vo.RoleInfo;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

@Component
@ProtoDesc(description = "添加角色响应")
public class AddRoleResp extends AbstractPacket {
    @Protobuf(description = "结果",required = true)
    private boolean result;
    @Protobuf(description = "增加的角色,result为true时存在")
    private RoleInfo roleInfo;

    public AddRoleResp setResult(boolean result) {
        this.result = result;
        return this;
    }

    public AddRoleResp setRoleInfo(RoleInfo roleInfo) {
        this.roleInfo = roleInfo;
        return this;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.ADD_ROLE_RESP;
    }
}
