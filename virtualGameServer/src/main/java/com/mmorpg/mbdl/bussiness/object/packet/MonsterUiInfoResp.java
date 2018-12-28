package com.mmorpg.mbdl.bussiness.object.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

/**
 * 怪物提供给前端的ui信息
 *
 * @author Sando Geek
 * @since v1.0 2018/12/28
 **/
@Component
@ProtoDesc(description = "怪物提供给前端的ui信息响应包")
public class MonsterUiInfoResp extends AbstractPacket {
    @Protobuf(description = "怪物id",required = true)
    private long objId;
    @Protobuf(description = "怪物名称",required = true)
    private String name;
    @Protobuf(description = "当前血量",required = true)
    private long currentHp;
    @Protobuf(description = "最大血量",required = true)
    private long maxHp;

    public MonsterUiInfoResp() {
    }

    public MonsterUiInfoResp(long objId, String name, long currentHp, long maxHp) {
        this.objId = objId;
        this.name = name;
        this.currentHp = currentHp;
        this.maxHp = maxHp;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.MONSTER_UI_INFO_RESP;
    }
}