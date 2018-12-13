package com.mmorpg.mbdl.bussiness.object.model;

/**
 * 抽象生物
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public abstract class AbstractCreature extends AbstractVisibleObject {
    /**
     * 血量
     */
    private long hp;
    /**
     * 蓝量
     */
    private long mp;

    public long getHp() {
        return hp;
    }

    public AbstractCreature setHp(long hp) {
        this.hp = hp;
        return this;
    }

    public long getMp() {
        return mp;
    }

    public AbstractCreature setMp(long mp) {
        this.mp = mp;
        return this;
    }
}
