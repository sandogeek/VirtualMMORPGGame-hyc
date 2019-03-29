package com.mmorpg.mbdl.business.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

/**
 * 选择角色请求
 *
 * @author Sando Geek
 * @since v1.0 2018/12/19
 **/
@Component
@ProtoDesc(description = "选择角色请求")
public class ChooseRoleReq extends AbstractPacket {
    @Protobuf(description = "角色昵称",required = true)
    private String name;

    public String getName() {
        return name;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.CHOOSE_ROLE_REQ;
    }
}
