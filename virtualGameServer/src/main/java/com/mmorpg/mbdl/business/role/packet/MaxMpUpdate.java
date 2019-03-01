package com.mmorpg.mbdl.business.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 最大蓝量更新包
 *
 * @author Sando Geek
 * @since v1.0 2019/3/1
 **/
@ProtoDesc(description = "最大蓝量更新包")
public class MaxMpUpdate extends AbstractPacket {
    @Protobuf(description = "最大蓝量")
    private long maxMp;

    public MaxMpUpdate() {
    }

    public MaxMpUpdate(long maxMp) {
        this.maxMp = maxMp;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.MAX_MP_UPDATE;
    }
}