package com.mmorpg.mbdl.business.container.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 使用物品响应
 *
 * @author Sando Geek
 * @since v1.0 2019/1/29
 **/
@ProtoDesc(description = "使用物品响应")
public class UseItemResp extends AbstractPacket {
    @Protobuf(description = "结果")
    private boolean result;

    public UseItemResp() {
    }

    public UseItemResp setResult(boolean result) {
        this.result = result;
        return this;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.USE_ITEM_RESP;
    }
}