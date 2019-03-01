package com.mmorpg.mbdl.business.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 当前血量更新包
 *
 * @author Sando Geek
 * @since v1.0 2019/3/1
 **/
@ProtoDesc(description = "当前血量更新包")
public class CurrentHpUpdate extends AbstractPacket {
    @Protobuf(description = "当前血量")
    private long currentHp;

    public CurrentHpUpdate() {
    }

    public CurrentHpUpdate(long currentHp) {
        this.currentHp = currentHp;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.CURRENT_HP_UPDATE;
    }
}