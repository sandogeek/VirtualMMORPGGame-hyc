package com.mmorpg.mbdl.business.object.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 怪物最大血量更新
 *
 * @author Sando Geek
 * @since v1.0 2019/3/5
 **/
@ProtoDesc(description = "怪物最大血量更新")
public class MonsterMaxHpUpdate extends AbstractPacket {
    @Protobuf(description = "怪物id", required = true)
    private long objId;
    @Protobuf(description = "怪物最大血量", required = true)
    private long maxHp;

    public MonsterMaxHpUpdate() {
    }

    public MonsterMaxHpUpdate(long objId, long maxHp) {
        this.objId = objId;
        this.maxHp = maxHp;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.MONSTER_MAX_HP_UPDATE;
    }
}