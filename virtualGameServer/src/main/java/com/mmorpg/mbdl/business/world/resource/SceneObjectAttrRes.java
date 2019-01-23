package com.mmorpg.mbdl.business.world.resource;

import com.mmorpg.mbdl.business.object.model.SceneObjectType;
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
    private int objectKey;
    private String name;
    private SceneObjectType objectType;
    private long maxHp;
    private long maxMp;
    private int attack;
    private int defence;

    public int getObjectKey() {
        return objectKey;
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
