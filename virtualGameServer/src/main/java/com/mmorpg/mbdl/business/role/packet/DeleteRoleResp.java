package com.mmorpg.mbdl.business.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

/**
 * 删除角色响应
 *
 * @author Sando Geek
 * @since v1.0 2018/12/19
 **/
@Component
@ProtoDesc(description = "删除角色响应")
public class DeleteRoleResp extends AbstractPacket {
    @Protobuf(required = true,description = "结果")
    private boolean result;
    @Protobuf(description = "已删除角色的名称")
    private String nameDelete;

    public DeleteRoleResp setResult(boolean result) {
        this.result = result;
        return this;
    }

    public DeleteRoleResp setNameDelete(String nameDelete) {
        this.nameDelete = nameDelete;
        return this;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.DELETE_ROLE_RESP;
    }
}
