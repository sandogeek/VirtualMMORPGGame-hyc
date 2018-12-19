package com.mmorpg.mbdl.bussiness.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

/**
 * 删除角色请求
 *
 * @author Sando Geek
 * @since v1.0 2018/12/19
 **/
@Component
@ProtoDesc(description = "删除角色请求")
public class DeleteRoleReq extends AbstractPacket {
    @Protobuf(description = "待删除角色的名称")
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.DELETE_ROLE_REQ;
    }
}
