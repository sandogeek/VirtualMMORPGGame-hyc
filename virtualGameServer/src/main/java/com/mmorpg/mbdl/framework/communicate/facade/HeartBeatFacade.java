package com.mmorpg.mbdl.framework.communicate.facade;

import com.mmorpg.mbdl.framework.communicate.packet.PingHeartBeat;
import com.mmorpg.mbdl.framework.communicate.packet.PongHeartBeat;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketMethod;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import com.mmorpg.mbdl.framework.communicate.websocket.model.SessionState;

@PacketHandler
public class HeartBeatFacade {
    @PacketMethod(logOrNot = false,state = SessionState.ANY)
    public PongHeartBeat handlePing(ISession session, PingHeartBeat pingHeartBeat){
        return new PongHeartBeat();
    }

    // @PacketMethod(logOrNot = true,state = SessionState.ANY)
    // public void handlePong(ISession session, PongHeartBeat pongHeartBeat){
    //
    // }

}
