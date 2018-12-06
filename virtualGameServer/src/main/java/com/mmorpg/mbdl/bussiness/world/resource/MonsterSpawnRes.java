package com.mmorpg.mbdl.bussiness.world.resource;

import com.mmorpg.mbdl.bussiness.world.model.MonsterSpawn;
import com.mmorpg.mbdl.framework.resource.annotation.Id;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

import java.util.List;

/**
 * 怪物出生静态资源
 *
 * @author Sando Geek
 * @since v1.0 2018/12/6
 **/
@ResDef(suffix = ".json")
public class MonsterSpawnRes {
    @Id
    private int mapId;
    private List<MonsterSpawn> monsterSpawnDatas;

    public int getMapId() {
        return mapId;
    }

    public List<MonsterSpawn> getMonsterSpawnDatas() {
        return monsterSpawnDatas;
    }
}
