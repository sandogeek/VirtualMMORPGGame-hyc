package com.mmorpg.mbdl.business.role.facade;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.mmorpg.mbdl.business.role.event.RoleLogoutEvent;
import com.mmorpg.mbdl.business.role.packet.*;
import com.mmorpg.mbdl.business.role.service.RoleService;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.annotation.PacketMethod;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import com.mmorpg.mbdl.framework.communicate.websocket.model.SessionState;
import com.mmorpg.mbdl.framework.event.preset.SessionCloseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 角色门面
 *
 * @author Sando Geek
 * @since v1.0 2018/12/17
 **/
@PacketHandler
public class RoleFacade {
    private static Logger logger = LoggerFactory.getLogger(RoleFacade.class);

    @Autowired
    private RoleService roleService;

    @PacketMethod(state = SessionState.LOGINED)
    public GetRoleListResp handleGetRoleListReq(ISession session, GetRoleListReq getRoleListReq) {
        return roleService.handleGetRoleListReq(session,getRoleListReq);
    }

    @PacketMethod(state = SessionState.LOGINED)
    public AddRoleResp handleAddRoleReq(ISession session, AddRoleReq addRoleReq) {
        return roleService.handleAddRoleReq(session,addRoleReq);
    }

    @PacketMethod(state = SessionState.LOGINED)
    public DeleteRoleResp handleDeleteRoleReq(ISession session, DeleteRoleReq deleteRoleReq) {
        return roleService.handleDeleteRoleReq(session,deleteRoleReq);
    }

    @PacketMethod(state = SessionState.LOGINED)
    public ChooseRoleResp handleChooseRoleReq(ISession session, ChooseRoleReq chooseRoleReq) {
        return roleService.handleChooseRoleReq(session,chooseRoleReq);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleSessionClose(SessionCloseEvent sessionCloseEvent){
        roleService.handleSessionClose(sessionCloseEvent);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void handleLogoutEvent(RoleLogoutEvent roleLogoutEvent){
        roleService.handleRoleLogoutEvent(roleLogoutEvent);
    }
}
