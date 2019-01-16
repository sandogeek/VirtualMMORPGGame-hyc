package com.mmorpg.mbdl.business.world.facade;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.mmorpg.mbdl.business.role.event.RoleLogoutEvent;
import com.mmorpg.mbdl.business.world.packet.EnterWorldReq;
import com.mmorpg.mbdl.business.world.scene.packet.SwitchSceneReq;
import com.mmorpg.mbdl.business.world.service.WorldService;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 世界模块门面
 *
 * @author Sando Geek
 * @since v1.0 2018/12/24
 **/
@PacketHandler
public class WorldFacade {
    @Autowired
    private WorldService worldService;

    public void handleEnterWorldReq(ISession session, EnterWorldReq enterWorldReq){
        worldService.handleEnterWorldReq(session, enterWorldReq);
    }
    public void handleSwitchSceneReq(ISession session, SwitchSceneReq switchSceneReq){
        worldService.handleSwitchSceneReq(session, switchSceneReq);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleLogoutEvent(RoleLogoutEvent roleLogoutEvent) {
        worldService.handleLogoutEvent(roleLogoutEvent);
    }
}
