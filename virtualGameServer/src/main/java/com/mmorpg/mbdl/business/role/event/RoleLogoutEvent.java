package com.mmorpg.mbdl.business.role.event;

import com.mmorpg.mbdl.business.object.model.Role;

/**
 * 角色下线事件
 *
 * @author Sando Geek
 * @since v1.0 2018/12/29
 **/
public class RoleLogoutEvent {
    private Role role;

    public RoleLogoutEvent(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }
}
