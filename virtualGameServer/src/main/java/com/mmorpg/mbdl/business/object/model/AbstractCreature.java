package com.mmorpg.mbdl.business.object.model;

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

    private long maxHp;

    private long maxMp;

    private int attack;

    private int defence;

    public void init() {

    }

    public AbstractCreature setFullHp(){
        setCurrentHp(maxHp);
        return this;
    }

    public AbstractCreature setFullMp(){
        setCurrentMp(maxMp);
        return this;
    }

    public int getAttack() {
        return attack;
    }

    public AbstractCreature setAttack(int attack) {
        this.attack = attack;
        return this;
    }

    public int getDefence() {
        return defence;
    }

    public AbstractCreature setDefence(int defence) {
        this.defence = defence;
        return this;
    }

    public long getMaxHp() {
        return maxHp;
    }

    public AbstractCreature setMaxHp(long maxHp) {
        this.maxHp = maxHp;
        return this;
    }

    public long getMaxMp() {
        return maxMp;
    }

    public AbstractCreature setMaxMp(long maxMp) {
        this.maxMp = maxMp;
        return this;
    }

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
