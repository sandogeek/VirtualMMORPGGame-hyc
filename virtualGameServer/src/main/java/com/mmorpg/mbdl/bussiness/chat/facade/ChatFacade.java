package com.mmorpg.mbdl.bussiness.chat.facade;

import com.mmorpg.mbdl.bussiness.chat.packet.ChatReq;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketMethod;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WsSession;

@PacketHandler
public class ChatFacade {
    @PacketMethod(executeParallel = true)
    public ChatReq handleChatReq(WsSession session, ChatReq chatReq){
        return new ChatReq();
    }
}
