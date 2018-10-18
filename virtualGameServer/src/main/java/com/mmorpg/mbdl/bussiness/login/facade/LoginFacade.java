package com.mmorpg.mbdl.bussiness.login.facade;

import com.mmorpg.mbdl.bussiness.chat.packet.ChatReq;
import com.mmorpg.mbdl.bussiness.login.packet.LoginAuthReq;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WSession;

@PacketHandler
public class LoginFacade {

    public void loginAuth(WSession session, LoginAuthReq req){

    }
    public void handleChatReq(WSession session, ChatReq chatReq){

    }
}
