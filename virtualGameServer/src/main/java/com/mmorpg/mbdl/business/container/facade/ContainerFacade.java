package com.mmorpg.mbdl.business.container.facade;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.mmorpg.mbdl.business.container.service.ContainerService;
import com.mmorpg.mbdl.business.role.event.RoleLogoutEvent;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;

/**
 * @author Sando Geek
 * @since v1.0 2019/1/15
 **/
@PacketHandler
public class ContainerFacade {
    @Subscribe
    @AllowConcurrentEvents
    public void handleLogoutEvent(RoleLogoutEvent roleLogoutEvent){
        ContainerService.getInstance().handleRoleLogoutEvent(roleLogoutEvent);
    }
}
