package com.mmorpg.mbdl.business.chat.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.chat.model.ChatType;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

@ProtoDesc(description = "聊天请求")
public class ChatReq extends AbstractPacket {
    @Protobuf(description = "聊天目标Id,为0表示向世界频道发言",required = true)
    private ChatType chatType;
    @Protobuf(description = "发言内容",required = true)
    private String content;

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
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
