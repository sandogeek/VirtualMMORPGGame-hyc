package com.mmorpg.mbdl.bussiness.world.manager;

import com.mmorpg.mbdl.bussiness.object.creator.ObjectCreatorManager;
import com.mmorpg.mbdl.bussiness.object.model.AbstractVisibleSceneObject;
import com.mmorpg.mbdl.bussiness.world.resource.BornRes;
import com.mmorpg.mbdl.bussiness.world.resource.SceneObjectAttrRes;
import com.mmorpg.mbdl.bussiness.world.scene.model.Scene;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 怪物出生管理器
 *
 * @author Sando Geek
 * @since v1.0 2018/12/13
 **/
@Component
public class SpawnManager {
    @Autowired
    protected IStaticRes<Integer, SceneObjectAttrRes> sceneObjectAttrResMap;
    @Autowired
    private IStaticRes<Integer, BornRes> bornResMap;
    @Autowired
    private ObjectCreatorManager objectCreatorManager;

    /**
     * 让所有怪物出生到相应的场景
     * @param scenes
     */
    public void spawnAll(Collection<Scene> scenes){
        scenes.forEach(scene -> {
            int sceneId = scene.getSceneId();
            bornResMap.get(sceneId).getBornDataList().forEach(bornData -> {
                SceneObjectAttrRes sceneObjectAttrRes = sceneObjectAttrResMap.get(bornData.getObjectKey());
                AbstractVisibleSceneObject visibleSceneObject
                        = objectCreatorManager.getCreatorByObjectType(sceneObjectAttrRes.getObjectType()).create(sceneId, bornData);
                scene.appearInScene(visibleSceneObject);
            });

        });
    }
}
