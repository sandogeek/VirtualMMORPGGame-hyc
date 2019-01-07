package com.mmorpg.mbdl.bussiness.chat.packet;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.ProtoDesc;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

@ProtoDesc(description = "聊天响应")
public class ChatResp extends AbstractPacket {
    @Protobuf(description = "发送结果",required = true)
    private boolean result;

    public ChatResp setResult(boolean result) {
        this.result = result;
        return this;
    }

    @Override
    public short getPacketId() {
        return PacketIdManager.CHAT_RESP;
    }
}
