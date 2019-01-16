package com.mmorpg.mbdl.business.world;

import com.mmorpg.mbdl.business.object.model.AbstractVisibleSceneObject;
import com.mmorpg.mbdl.business.world.manager.SceneManager;
import org.springframework.stereotype.Component;

/**
 * 世界，包含并管理所有场景
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
@Component
public class World {

    /**
     * 让可见物出生到世界
     * @param visibleSceneObject 可见物
     */
    public void born(AbstractVisibleSceneObject visibleSceneObject) {
        SceneManager.getInstance()
                .getSceneBySceneId(visibleSceneObject.getSceneId()).appearInScene(visibleSceneObject);
    }
}
