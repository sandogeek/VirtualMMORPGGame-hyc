package com.mmorpg.mbdl.business.chat.facade;

import com.mmorpg.mbdl.business.chat.packet.ChatReq;
import com.mmorpg.mbdl.business.chat.service.ChatService;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;

@PacketHandler
public class ChatFacade {

    public void handleChatReq(ISession session, ChatReq chatReq){
        ChatService.getInstance().handleChatReq(session,chatReq);
    }
}
