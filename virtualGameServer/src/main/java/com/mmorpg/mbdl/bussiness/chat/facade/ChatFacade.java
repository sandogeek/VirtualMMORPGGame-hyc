package com.mmorpg.mbdl.bussiness.chat.facade;

import com.mmorpg.mbdl.bussiness.chat.packet.ChatReq;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WSession;

@PacketHandler
public class ChatFacade {
    public void handleChatReq(WSession session, ChatReq chatReq){

    }
}
