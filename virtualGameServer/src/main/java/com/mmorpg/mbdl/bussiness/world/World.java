package com.mmorpg.mbdl.bussiness.world;

import com.mmorpg.mbdl.bussiness.world.scene.Scene;

import java.util.Map;

/**
 * 世界，包含并管理所有场景
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public class World {
    private Map<Integer, Scene> sceneId2SceneMap;
}
