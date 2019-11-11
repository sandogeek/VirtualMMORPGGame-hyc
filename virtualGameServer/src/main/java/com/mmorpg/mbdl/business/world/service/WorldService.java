package com.mmorpg.mbdl.business.world.service;

import com.mmorpg.mbdl.business.role.event.RoleLogoutEvent;
import com.mmorpg.mbdl.business.role.manager.RoleManager;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.business.shop.res.ConditionTestRes;
import com.mmorpg.mbdl.business.world.World;
import com.mmorpg.mbdl.business.world.manager.SceneManager;
import com.mmorpg.mbdl.business.world.packet.EnterWorldReq;
import com.mmorpg.mbdl.business.world.scene.packet.SwitchSceneReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 世界服务
 *
 * @author Sando Geek
 * @since v1.0 2018/12/24
 **/
@Component
public class WorldService {

    private static WorldService self;

    @Autowired
    private World world;
    @Autowired
    private RoleManager roleManager;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static WorldService getInstance() {
        return self;
    }

    public void handleEnterWorldReq(Role role, EnterWorldReq enterWorldReq) {
        world.born(role);
    }
    public void handleSwitchSceneReq(Role role, SwitchSceneReq switchSceneReq) {
        SceneManager.getInstance().switchToSceneById(role, switchSceneReq.getTargetSceneId());
    }

    public void handleLogoutEvent(RoleLogoutEvent roleLogoutEvent) {
        Role role = roleLogoutEvent.getRole();
        ConditionTestRes conditionTestRes = roleManager.getConditionTestResIStaticRes().get(1);
        SceneManager.getInstance().getSceneBySceneId(role.getSceneId()).disappearInScene(role);
    }
}
