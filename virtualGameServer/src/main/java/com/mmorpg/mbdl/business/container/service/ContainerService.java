package com.mmorpg.mbdl.business.container.service;

import com.mmorpg.mbdl.business.container.manager.ContainerManager;
import com.mmorpg.mbdl.business.role.event.RoleLogoutEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 容器服务
 *
 * @author Sando Geek
 * @since v1.0 2019/1/15
 **/
@Component
public class ContainerService {
    private static ContainerService self;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static ContainerService getInstance() {
        return self;
    }

    public void handleRoleLogoutEvent(RoleLogoutEvent roleLogoutEvent) {
        ContainerManager.getInstance().updateEntity(roleLogoutEvent.getRole().getContainerEntity());
    }
}
