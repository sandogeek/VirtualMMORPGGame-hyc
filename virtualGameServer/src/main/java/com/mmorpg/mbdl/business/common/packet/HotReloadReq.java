package com.mmorpg.mbdl.business.common.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 热更请求
 *
 * @author Sando Geek
 * @since v1.0 2019/7/21
 **/
@ProtoDesc(description = "热更请求")
public class HotReloadReq extends AbstractPacket {
    @Protobuf(description = "文件名")
    private String fileName;
    @Protobuf(description = "数据")
    private byte[] data;

    public HotReloadReq() {
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.HOT_RELOAD_REQ;
    }
}