package com.mmorpg.mbdl.bussiness.object.model;

/**
 * 抽象生物
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public abstract class AbstractCreature extends AbstractVisibleSceneObject {
    /**
     * 当前血量
     */
    private long currentHp;
    /**
     * 当前蓝量
     */
    private long currentMp;

    public long getCurrentHp() {
        return currentHp;
    }

    public AbstractCreature setCurrentHp(long currentHp) {
        this.currentHp = currentHp;
        return this;
    }

    public long getCurrentMp() {
        return currentMp;
    }

    public AbstractCreature setCurrentMp(long currentMp) {
        this.currentMp = currentMp;
        return this;
    }
}
