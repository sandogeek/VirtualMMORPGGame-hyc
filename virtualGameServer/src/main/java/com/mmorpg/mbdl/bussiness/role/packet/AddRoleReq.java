package com.mmorpg.mbdl.bussiness.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.bussiness.role.model.RoleType;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

/**
 * 添加角色请求
 *
 * @author Sando Geek
 * @since v1.0 2018/12/17
 **/
@Component
@ProtoDesc(description = "添加角色请求")
public class AddRoleReq extends AbstractPacket {
    @Protobuf(description = "角色昵称",required = true)
    private String roleName;
    @Protobuf(description = "角色类型",required = true)
    private RoleType roleType;


    @Override
    public short getPacketId() {
        return PacketIdManager.ADD_ROLE_REQ;
    }
}
