package com.mmorpg.mbdl.business.container.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.business.container.packet.VO.ItemUiInfo;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * 物品增加响应
 *
 * @author Sando Geek
 * @since v1.0 2019/2/21
 **/
@ProtoDesc(description = "物品增加响应")
public class ItemAddResp extends AbstractPacket {
    @Protobuf(description = "增加的物品",required = true)
    private List<ItemUiInfo> itemUiInfoList = new ArrayList<>();

    public ItemAddResp() {
    }

    public List<ItemUiInfo> getItemUiInfoList() {
        return itemUiInfoList;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.ITEM_ADD_RESP;
    }
}