package com.mmorpg.mbdl.bussiness.chat.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

public class ChatReq extends AbstractPacket {
    @Protobuf(description = "聊天频道id")
    private int id;
    @Protobuf(description = "发言内容")
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.CHATREQ;
    }
}
