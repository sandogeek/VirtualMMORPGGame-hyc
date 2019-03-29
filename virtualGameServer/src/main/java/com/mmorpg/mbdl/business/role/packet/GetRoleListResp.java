package com.mmorpg.mbdl.business.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.business.role.packet.vo.RoleInfo;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取角色列表响应
 *
 * @author Sando Geek
 * @since v1.0 2018/12/14
 **/
@Component
@ProtoDesc(description = "获取角色列表响应")
public class GetRoleListResp extends AbstractPacket {
    @Protobuf(description = "角色列表信息",required = true)
    private List<RoleInfo> roleInfoList = new ArrayList<>(3);

    public List<RoleInfo> getRoleInfoList() {
        return roleInfoList;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.GET_ROLE_LIST_RESP;
    }
}
