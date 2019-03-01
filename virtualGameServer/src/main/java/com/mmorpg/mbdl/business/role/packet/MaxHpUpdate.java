package com.mmorpg.mbdl.business.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
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
    @Protobuf(description = "")
    private long maxHp;

    public MaxHpUpdate() {
    }

    public MaxHpUpdate(long maxHp) {
        this.maxHp = maxHp;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.MAX_HP_UPDATE;
    }
}