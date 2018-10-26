package com.mmorpg.mbdl.bussiness.login.facade;

import com.mmorpg.mbdl.bussiness.login.packet.LoginAuthReq;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WsSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PacketHandler
public class LoginFacade {
    private static final Logger logger = LoggerFactory.getLogger(LoginFacade.class);
    public void loginAuth(WsSession session, LoginAuthReq req){
        // TODO 密码采用非对称加密传输并存储到数据库
        String message = String.format("协议[%s-%s]分发成功：帐号：%s,密码：%s",req.getPacketId(),req.getClass().getSimpleName(),req.getAccount(),req.getPassword());
        logger.debug(message);
    }
    // public void handleChatReq(WsSession session, ChatReq chatReq){
    //
    // }
}
