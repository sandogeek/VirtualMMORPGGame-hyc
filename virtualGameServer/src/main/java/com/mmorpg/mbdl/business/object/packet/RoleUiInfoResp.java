package com.mmorpg.mbdl.business.object.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.business.role.model.RoleType;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

/**
 * Role提供给前端的显示信息
 *
 * @author Sando Geek
 * @since v1.0 2018/12/21
 **/
@Component
@ProtoDesc(description = "角色UI信息响应包")
public class RoleUiInfoResp extends AbstractPacket {
    @Protobuf(required = true,description = "角色id")
    private long roleId;
    @Protobuf(required = true,description = "角色名称")
    private String name;
    @Protobuf(required = true,description = "角色等级")
    private int level;
    @Protobuf(required = true,description = "角色类型")
    private RoleType roleType;



    public RoleUiInfoResp setRoleId(long roleId) {
        this.roleId = roleId;
        return this;
    }

    public RoleUiInfoResp setName(String name) {
        this.name = name;
        return this;
    }

    public RoleUiInfoResp setLevel(int level) {
        this.level = level;
        return this;
    }

    public RoleUiInfoResp setRoleType(RoleType roleType) {
        this.roleType = roleType;
        return this;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.ROLE_UI_INFO_RESP;
    }
}