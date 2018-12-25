package com.mmorpg.mbdl.bussiness.world.resource;

import com.mmorpg.mbdl.framework.resource.annotation.Id;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

import java.util.List;

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
    /**
     * 可前往的场景id集合
     */
    private List<Integer> canGo;

    public int getSceneId() {
        return sceneId;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getCanGo() {
        return canGo;
    }
}
