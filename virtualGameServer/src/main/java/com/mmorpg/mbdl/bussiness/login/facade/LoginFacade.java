package com.mmorpg.mbdl.bussiness.login.facade;

import com.mmorpg.mbdl.bussiness.login.packet.LoginAuthReq;
import com.mmorpg.mbdl.bussiness.login.packet.LoginResultResp;
import com.mmorpg.mbdl.bussiness.login.service.LoginService;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketMethod;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import com.mmorpg.mbdl.framework.communicate.websocket.model.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@PacketHandler
public class LoginFacade {
    private static final Logger logger = LoggerFactory.getLogger(LoginFacade.class);
    @Autowired
    private LoginService loginService;

    @PacketMethod(state = SessionState.CONNECTED)
    public LoginResultResp loginAuth(ISession session, LoginAuthReq req){
        return loginService.login(session,req);
    }
}
