package com.mmorpg.mbdl.business.container.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.business.container.packet.VO.ItemUiInfo;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取背包内容响应
 *
 * @author Sando Geek
 * @since v1.0 2019/1/29
 **/
@ProtoDesc(description = "获取背包内容响应")
public class GetPackContentResp extends AbstractPacket {
    @Protobuf(description = "所有物品信息表")
    private List<ItemUiInfo> itemUiInfoList = new ArrayList<>();

    public GetPackContentResp() {
    }

    public List<ItemUiInfo> getItemUiInfoList() {
        return itemUiInfoList;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.GET_PACK_CONTENT_RESP;
    }
}