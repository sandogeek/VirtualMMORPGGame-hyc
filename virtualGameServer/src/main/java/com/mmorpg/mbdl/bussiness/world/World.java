package com.mmorpg.mbdl.bussiness.world;

import com.mmorpg.mbdl.bussiness.object.model.AbstractVisibleObject;
import com.mmorpg.mbdl.bussiness.world.resource.SceneRes;
import com.mmorpg.mbdl.bussiness.world.scene.Scene;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 世界，包含并管理所有场景
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
@Component
public class World {
    private Map<Integer, Scene> sceneId2SceneMap = new HashMap<>(16);
    @Autowired
    private IStaticRes<Integer, SceneRes> id2Scene;

    @PostConstruct
    private void init() {
        id2Scene.values().forEach(sceneRes -> {
            Scene scene = new Scene().setName(sceneRes.getName())
                    .setSceneId(sceneRes.getSceneId());
            sceneId2SceneMap.put(scene.getSceneId(),scene);
        });
    }

    /**
     * 让可见物出生到世界
     * @param object 可见物
     */
    public void born(AbstractVisibleObject object) {

    }
}
