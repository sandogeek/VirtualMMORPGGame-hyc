package com.mmorpg.mbdl.bussiness.chat.facade;

import com.mmorpg.mbdl.bussiness.chat.packet.ChatReq;
import com.mmorpg.mbdl.bussiness.chat.service.ChatService;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;

@PacketHandler
public class ChatFacade {

    public void handleChatReq(ISession session, ChatReq chatReq){
        ChatService.getInstance().handleChatReq(session,chatReq);
    }
}
