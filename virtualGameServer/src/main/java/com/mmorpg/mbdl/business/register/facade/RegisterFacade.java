package com.mmorpg.mbdl.business.register.facade;

import com.mmorpg.mbdl.business.register.packet.RegisterReq;
import com.mmorpg.mbdl.business.register.packet.RegisterResp;
import com.mmorpg.mbdl.business.register.service.RegisterService;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketMethod;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import com.mmorpg.mbdl.framework.communicate.websocket.model.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author sando
 */
@PacketHandler
public class RegisterFacade {
    @Autowired
    private RegisterService registerService;
    private static Logger logger = LoggerFactory.getLogger(RegisterFacade.class);
    @PacketMethod(state = SessionState.CONNECTED)
    public RegisterResp register(ISession session, RegisterReq registerReq){
        return registerService.register(registerReq);
    }
}
