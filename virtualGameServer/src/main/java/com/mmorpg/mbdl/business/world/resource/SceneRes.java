package com.mmorpg.mbdl.business.world.resource;

import com.mmorpg.mbdl.framework.resource.annotation.Key;
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
    @Key
    private int sceneId;
    private String name;
    /**
     * 可前往的场景id集合
     */
    private List<Integer> canGoList;

    public int getSceneId() {
        return sceneId;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getCanGoList() {
        return canGoList;
    }
}
