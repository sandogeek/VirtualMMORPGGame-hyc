package com.mmorpg.mbdl.bussiness.world.resource;

import com.mmorpg.mbdl.bussiness.world.model.SpawnData;
import com.mmorpg.mbdl.framework.resource.annotation.Id;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.List;

/**
 * 怪物出生静态资源
 *
 * @author Sando Geek
 * @since v1.0 2018/12/6
 **/
@ResDef(suffix = ".json")
public class SpawnRes {
    @Id
    private int sceneId;
    private List<SpawnData> spawnDataList;

    public int getSceneId() {
        return sceneId;
    }

    @SuppressFBWarnings("UWF_UNWRITTEN_FIELD")
    public List<SpawnData> getSpawnDataList() {
        return spawnDataList;
    }
}
