package com.mmorpg.mbdl.business.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 最大血量更新包
 *
 * @author Sando Geek
 * @since v1.0 2019/3/1
 **/
@ProtoDesc(description = "最大血量更新包")
public class MaxHpUpdate extends AbstractPacket {
    @Protobuf(description = "角色id", required = true)
    private long roleId;
    @Protobuf(description = "最大血量", required = true)
    private long maxHp;

    public MaxHpUpdate() {
    }

    public MaxHpUpdate(long roleId, long maxHp) {
        this.roleId = roleId;
        this.maxHp = maxHp;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.MAX_HP_UPDATE;
    }
}