package com.mmorpg.mbdl.business.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 当前蓝量更新包
 *
 * @author Sando Geek
 * @since v1.0 2019/3/1
 **/
@ProtoDesc(description = "当前蓝量更新包")
public class CurrentMpUpdate extends AbstractPacket {
    @Protobuf(description = "角色id", required = true)
    private long roleId;
    @Protobuf(description = "当前蓝量", required = true)
    private long currentMp;

    public CurrentMpUpdate() {
    }

    public CurrentMpUpdate(long roleId, long currentMp) {
        this.roleId = roleId;
        this.currentMp = currentMp;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.CURRENT_MP_UPDATE;
    }
}