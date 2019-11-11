package com.mmorpg.mbdl.business.chat.facade;

import com.mmorpg.mbdl.business.chat.packet.ChatReq;
import com.mmorpg.mbdl.business.chat.service.ChatService;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;

@PacketHandler
public class ChatFacade {

    public void handleChatReq(Role role, ChatReq chatReq){
        ChatService.getInstance().handleChatReq(role, chatReq);
    }
}
