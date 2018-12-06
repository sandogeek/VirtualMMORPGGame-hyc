package com.mmorpg.mbdl.bussiness.register.facade;

import com.mmorpg.mbdl.bussiness.register.packet.RegisterReq;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;

/**
 * @author sando
 */
@PacketHandler
public class RegisterFacade {

    public void register(ISession session, RegisterReq regiserReq){

    }
}
