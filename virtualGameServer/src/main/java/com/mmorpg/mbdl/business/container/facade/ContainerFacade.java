package com.mmorpg.mbdl.business.container.facade;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.mmorpg.mbdl.business.container.packet.GetPackContentReq;
import com.mmorpg.mbdl.business.container.packet.UseItemReq;
import com.mmorpg.mbdl.business.container.service.ContainerService;
import com.mmorpg.mbdl.business.role.event.RoleLogoutEvent;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;

/**
 * @author Sando Geek
 * @since v1.0 2019/1/15
 **/
@PacketHandler
public class ContainerFacade {
    public void handleGetPackContentReq(Role role, GetPackContentReq getPackContentReq) {
        ContainerService.getInstance().handleGetPackContentReq(role, getPackContentReq);
    }

    public void handleUseItemReq(Role role, UseItemReq useItemReq) {
        ContainerService.getInstance().handleUseItemReq(role, useItemReq);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleLogoutEvent(RoleLogoutEvent roleLogoutEvent) {
        ContainerService.getInstance().handleRoleLogoutEvent(roleLogoutEvent);
    }
}
