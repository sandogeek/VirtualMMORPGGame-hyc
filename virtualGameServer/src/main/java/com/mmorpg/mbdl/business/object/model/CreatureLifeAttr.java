package com.mmorpg.mbdl.business.object.model;

/**
 * 生物的生命属性
 *
 * @author Sando Geek
 * @since v1.0 2019/1/16
 **/
public class CreatureLifeAttr {
    private AbstractCreature owner;

    public CreatureLifeAttr(AbstractCreature owner) {
        this.owner = owner;
    }

    /**
     * 当前血量
     */
    private long currentHp;
    /**
     * 当前蓝量
     */
    private long currentMp;

    private long maxHp;

    private long maxMp;

    public void setOwner(AbstractCreature owner) {
        this.owner = owner;
    }

    public long getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(long currentHp) {
        this.currentHp = currentHp;
    }

    public long getCurrentMp() {
        return currentMp;
    }

    public void setCurrentMp(long currentMp) {
        this.currentMp = currentMp;
    }

    public long getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(long maxHp) {
        this.maxHp = maxHp;
    }

    public long getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(long maxMp) {
        this.maxMp = maxMp;
    }
}
