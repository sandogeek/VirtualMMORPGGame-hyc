package com.mmorpg.mbdl.business.object.model;

/**
 * 生物的战斗属性
 *
 * @author Sando Geek
 * @since v1.0 2019/1/16
 **/
public class CreatureFightAttr {
    private AbstractCreature owner;

    public CreatureFightAttr(AbstractCreature owner) {
        this.owner = owner;
    }

    /**
     * 攻击力
     */
    private int attack;
    /**
     * 防御力
     */
    private int defence;

    private short level;

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public AbstractCreature getOwner() {
        return owner;
    }

    public void setOwner(AbstractCreature owner) {
        this.owner = owner;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }
}
