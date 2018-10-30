package com.mmorpg.mbdl.bussiness.login.facade;

import com.mmorpg.mbdl.bussiness.login.model.LoninResultType;
import com.mmorpg.mbdl.bussiness.login.packet.LoginAuthReq;
import com.mmorpg.mbdl.bussiness.login.packet.LoginResultResp;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketMethod;
import com.mmorpg.mbdl.framework.communicate.websocket.model.SessionState;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PacketHandler
public class LoginFacade {
    private static final Logger logger = LoggerFactory.getLogger(LoginFacade.class);
    @PacketMethod(state = SessionState.CONNECTED)
    public LoginResultResp loginAuth(WsSession session, LoginAuthReq req){
        // TODO 密码采用非对称加密传输并存储到数据库
        // String message = String.format("协议[%s-%s]分发成功：帐号：%s,密码：%s",req.getPacketId(),req.getClass().getSimpleName(),req.getAccount(),req.getPassword());
        // logger.debug(message);
        LoginResultResp loginResultResp = new LoginResultResp();
        loginResultResp.setResultType(LoninResultType.FAILURE);
        return loginResultResp;
    }
    // public void handleChatReq(WsSession session, ChatReq chatReq){
    //
    // }
}
