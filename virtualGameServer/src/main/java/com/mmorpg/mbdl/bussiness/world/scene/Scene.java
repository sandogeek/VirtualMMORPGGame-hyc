package com.mmorpg.mbdl.bussiness.world.scene;

import com.mmorpg.mbdl.bussiness.object.model.AbstractVisibleObject;

import java.util.Map;

/**
 * 场景
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public class Scene {
    /**
     * 场景id
     */
    private int sceneId;
    /**
     * 场景名称
     */
    private String name;

    private Map<Long, AbstractVisibleObject> objectId2Object;

    public int getSceneId() {
        return sceneId;
    }

    public Scene setSceneId(int sceneId) {
        this.sceneId = sceneId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Scene setName(String name) {
        this.name = name;
        return this;
    }
}
