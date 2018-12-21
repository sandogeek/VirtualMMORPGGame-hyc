package com.mmorpg.mbdl.bussiness.object.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.bussiness.role.model.RoleType;
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
    @Protobuf(required = true,description = "角色名称")
    private String name;
    @Protobuf(required = true,description = "角色等级")
    private int level;
    @Protobuf(required = true,description = "角色类型")
    private RoleType roleType;

    @Override
    public short getPacketId() {
        return PacketIdManager.ROLE_UI_INFO_RESP;
    }
}