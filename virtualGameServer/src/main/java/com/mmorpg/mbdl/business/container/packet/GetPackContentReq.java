package com.mmorpg.mbdl.business.container.packet;

import com.baidu.bjf.remoting.protobuf.annotation.ProtobufClass;
import com.mmorpg.mbdl.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 获取背包内容请求
 *
 * @author Sando Geek
 * @since v1.0 2019/1/29
 **/
@ProtobufClass
@ProtoDesc(description = "获取背包内容请求")
public class GetPackContentReq extends AbstractPacket {

    public GetPackContentReq() {
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.GET_PACK_CONTENT_REQ;
    }
}