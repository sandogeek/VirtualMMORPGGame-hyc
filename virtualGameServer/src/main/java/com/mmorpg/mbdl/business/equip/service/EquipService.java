package com.mmorpg.mbdl.business.equip.service;

import com.mmorpg.mbdl.business.role.event.RoleLogoutEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 装备服务
 *
 * @author Sando Geek
 * @since v1.0 2019/2/1
 **/
@Component
public class EquipService {
    private static EquipService self;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static EquipService getInstance() {
        return self;
    }

    public void handleRoleLogoutEvent(RoleLogoutEvent roleLogoutEvent) {

    }
}
