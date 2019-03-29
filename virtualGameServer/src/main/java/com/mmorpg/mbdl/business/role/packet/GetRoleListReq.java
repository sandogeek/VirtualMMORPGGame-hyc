package com.mmorpg.mbdl.business.role.packet;

/**
 * 获取角色列表请求
 *
 * @author Sando Geek
 * @since v1.0 2018/12/17
 **/

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

@Component
@ProtoDesc(description = "获取角色列表请求")
@ProtobufClass
public class GetRoleListReq extends AbstractPacket {

    @Override
    public short getPacketId() {
        return PacketIdManager.GET_ROLE_LIST_REQ;
    }
}
