package com.mmorpg.mbdl.business.role.resource;

import com.mmorpg.mbdl.framework.resource.annotation.Key;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

/**
 * 角色等级属性资源
 *
 * @author Sando Geek
 * @since v1.0 2018/12/29
 **/
@ResDef
public class RoleLevelRes {
    @Key
    private short level;
    private long maxHp;
    private long maxMp;
    private int attack;
    private short defence;
    /**
     * 达到下一级所需的经验
     */
    private long exp;

    public short getLevel() {
        return level;
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

    public short getDefence() {
        return defence;
    }

    public long getExp() {
        return exp;
    }
}
