package com.mmorpg.mbdl.bussiness.world.service;

import com.mmorpg.mbdl.bussiness.role.manager.RoleManager;
import com.mmorpg.mbdl.bussiness.world.World;
import com.mmorpg.mbdl.bussiness.world.packet.EnterWorldReq;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
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

    public void handleEnterWorldReq(ISession session, EnterWorldReq enterWorldReq) {
        world.born(roleManager.getRoleBySession(session));
    }
}
