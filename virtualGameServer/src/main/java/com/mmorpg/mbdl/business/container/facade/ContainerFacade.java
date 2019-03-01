package com.mmorpg.mbdl.business.container.facade;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.mmorpg.mbdl.business.container.packet.GetPackContentReq;
import com.mmorpg.mbdl.business.container.packet.UseItemReq;
import com.mmorpg.mbdl.business.container.service.ContainerService;
import com.mmorpg.mbdl.business.role.event.RoleLogoutEvent;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;

/**
 * @author Sando Geek
 * @since v1.0 2019/1/15
 **/
@PacketHandler
public class ContainerFacade {
    public void handleGetPackContentReq(ISession session, GetPackContentReq getPackContentReq) {
        ContainerService.getInstance().handleGetPackContentReq(session,getPackContentReq);
    }

    public void handleUseItemReq(ISession session, UseItemReq useItemReq) {
        ContainerService.getInstance().handleUseItemReq(session,useItemReq);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleLogoutEvent(RoleLogoutEvent roleLogoutEvent) {
        ContainerService.getInstance().handleRoleLogoutEvent(roleLogoutEvent);
    }
}
