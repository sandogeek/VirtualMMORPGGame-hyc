package com.mmorpg.mbdl.bussiness.chat.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import org.springframework.stereotype.Component;

@Component
@ProtoDesc(description = "聊天请求")
public class ChatReq extends AbstractPacket {
    @Protobuf(description = "聊天频道id")
    private int chatChannelId;
    @Protobuf(description = "发言内容")
    private String content;

    public int getChatChannelId() {
        return chatChannelId;
    }

    public void setChatChannelId(int chatChannelId) {
        this.chatChannelId = chatChannelId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.CHAT_REQ;
    }
}
