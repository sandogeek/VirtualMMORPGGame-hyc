package com.mmorpg.mbdl.business.container.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 物品使用请求
 *  物品Id和key有其一即可
 * @author Sando Geek
 * @since v1.0 2019/1/29
 **/
@ProtoDesc(description = "物品使用请求")
public class UseItemReq extends AbstractPacket {
    @Protobuf(description = "物品id")
    private long objectId;
    @Protobuf(description = "物品key")
    private int key;
    @Protobuf(description = "使用数量")
    private int amount;

    public UseItemReq() {
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.USE_ITEM_REQ;
    }
}