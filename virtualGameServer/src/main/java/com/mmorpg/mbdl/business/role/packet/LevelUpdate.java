package com.mmorpg.mbdl.business.role.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 等级属性更新包
 *
 * @author Sando Geek
 * @since v1.0 2019/3/1
 **/
@ProtoDesc(description = "等级属性更新包")
public class LevelUpdate extends AbstractPacket {
    @Protobuf(description = "角色id", required = true)
    private long roleId;
    @Protobuf(description = "等级")
    private int level;

    public LevelUpdate() {
    }

    public LevelUpdate(long roleId, int level) {
        this.roleId = roleId;
        this.level = level;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.LEVEL_UPDATE;
    }
}