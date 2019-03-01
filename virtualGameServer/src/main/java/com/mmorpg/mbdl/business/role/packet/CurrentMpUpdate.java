package com.mmorpg.mbdl.business.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
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
    @Protobuf(description = "")
    private long currentMp;

    public CurrentMpUpdate() {
    }

    public CurrentMpUpdate(long currentMp) {
        this.currentMp = currentMp;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.CURRENT_MP_UPDATE;
    }
}