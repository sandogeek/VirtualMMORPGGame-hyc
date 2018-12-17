package com.mmorpg.mbdl.bussiness.role.packet;

/**
 * 添加角色响应
 *
 * @author Sando Geek
 * @since v1.0 2018/12/17
 **/

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

@Component
@ProtoDesc(description = "添加角色响应")
public class AddRoleResp extends AbstractPacket {
    @Protobuf(description = "结果")
    private boolean result;


    @Override
    public short getPacketId() {
        return PacketIdManager.ADD_ROLE_RESP;
    }
}
