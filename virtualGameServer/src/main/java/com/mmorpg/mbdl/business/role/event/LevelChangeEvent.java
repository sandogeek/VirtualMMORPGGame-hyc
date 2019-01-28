package com.mmorpg.mbdl.business.role.event;

import com.mmorpg.mbdl.business.role.model.Role;

/**
 * 等级变更事件
 *
 * @author Sando Geek
 * @since v1.0 2019/1/28
 **/
public class LevelChangeEvent {
    private Role role;

    public LevelChangeEvent(Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }
}
