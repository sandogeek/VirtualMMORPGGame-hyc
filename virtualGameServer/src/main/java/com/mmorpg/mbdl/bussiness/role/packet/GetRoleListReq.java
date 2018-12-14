package com.mmorpg.mbdl.bussiness.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.bussiness.role.packet.vo.RoleInfo;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 获取角色列表请求
 *
 * @author Sando Geek
 * @since v1.0 2018/12/14
 **/
@Component
@ProtoDesc(description = "获取角色列表请求")
public class GetRoleListReq extends AbstractPacket {
    @Protobuf(description = "角色列表信息")
    private List<RoleInfo> roleInfoList;


    @Override
    public short getPacketId() {
        return PacketIdManager.GET_ROLE_LIST_REQ;
    }
}
