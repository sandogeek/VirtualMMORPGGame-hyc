package com.mmorpg.mbdl.bussiness.world.resource;

import com.mmorpg.mbdl.bussiness.object.model.SceneObjectType;
import com.mmorpg.mbdl.framework.resource.annotation.Id;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

/**
 * 场景可见物属性资源
 *
 * @author Sando Geek
 * @since v1.0 2018/12/28
 **/
@ResDef
public class SceneObjectAttrRes {
    @Id
    private int objectId;
    private String name;
    private SceneObjectType objectType;
    private long maxHp;
    private long maxMp;
    private int attack;
    private int defence;

    public int getObjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public SceneObjectType getObjectType() {
        return objectType;
    }

    public long getMaxHp() {
        return maxHp;
    }

    public long getMaxMp() {
        return maxMp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefence() {
        return defence;
    }
}
