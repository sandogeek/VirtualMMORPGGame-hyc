package com.mmorpg.mbdl.business.object.model;

/**
 * 抽象生物
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public abstract class AbstractCreature extends AbstractVisibleSceneObject {
    private CreatureLifeAttr lifeAttr;
    private CreatureFightAttr fightAttr;

    public AbstractCreature(Long objectId, String name,CreatureLifeAttr lifeAttr,CreatureFightAttr fightAttr) {
        super(objectId, name);
    }


    public CreatureLifeAttr getLifeAttr() {
        return lifeAttr;
    }

    public CreatureFightAttr getFightAttr() {
        return fightAttr;
    }

}
