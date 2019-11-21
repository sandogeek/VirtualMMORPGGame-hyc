package com.mmorpg.mbdl.business.chat.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.business.chat.model.ChatType;
import com.mmorpg.mbdl.business.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 聊天消息
 *
 * @author Sando Geek
 * @since v1.0 2019/1/7
 **/
@ProtoDesc(description = "聊天消息")
public class ChatMessage extends AbstractPacket {
    @Protobuf(description = "发送者Id",required = true)
    private long sourceRoleId;
    @Protobuf(description = "发送者名称",required = true)
    private String name;
    @Protobuf(description = "0 世界频道，空 个人频道，否则 组队id")
    private ChatType chatType;
    @Protobuf(description = "内容",required = true)
    private String content;

    public ChatMessage() {
    }

    public ChatMessage(long sourceRoleId, String name,ChatType chatType, String content) {
        this.sourceRoleId = sourceRoleId;
        this.name = name;
        this.chatType = chatType;
        this.content = content;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.CHAT_MESSAGE;
    }
}