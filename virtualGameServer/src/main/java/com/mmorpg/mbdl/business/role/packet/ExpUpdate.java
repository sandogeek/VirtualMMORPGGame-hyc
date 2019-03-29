package com.mmorpg.mbdl.business.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 经验属性更新包
 *
 * @author Sando Geek
 * @since v1.0 2019/3/1
 **/
@ProtoDesc(description = "经验属性更新包")
public class ExpUpdate extends AbstractPacket {
    @Protobuf(description = "当前经验")
    private long exp;

    public ExpUpdate() {
    }

    public ExpUpdate(long exp) {
        this.exp = exp;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.EXP_UPDATE;
    }
}