package com.mmorpg.mbdl.business.equip.facade;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.mmorpg.mbdl.business.equip.service.EquipService;
import com.mmorpg.mbdl.business.role.event.RoleLogoutEvent;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;

/**
 * 装备门面
 *
 * @author Sando Geek
 * @since v1.0 2019/2/1
 **/
@PacketHandler
public class EquipFacade {


    @Subscribe
    @AllowConcurrentEvents
    public void handleLogoutEvent(RoleLogoutEvent roleLogoutEvent){
        EquipService.getInstance().handleRoleLogoutEvent(roleLogoutEvent);
    }
}
