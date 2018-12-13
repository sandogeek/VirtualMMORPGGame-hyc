package com.mmorpg.mbdl.bussiness.world.resource;

import com.mmorpg.mbdl.framework.resource.annotation.Id;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

/**
 * 场景资源
 *
 * @author Sando Geek
 * @since v1.0 2018/12/12
 **/
@ResDef
public class SceneRes {
    @Id
    private int sceneId;
    private String name;

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
