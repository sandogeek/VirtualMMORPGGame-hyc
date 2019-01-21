package com.mmorpg.mbdl.business.object.model;

import com.mmorpg.mbdl.business.object.packet.MonsterUiInfoResp;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 怪物
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public class Monster extends AbstractCreature {


    public Monster(Long objectId, String name, CreatureLifeAttr lifeAttr, CreatureFightAttr fightAttr) {
        super(objectId, name, lifeAttr, fightAttr);
    }

    @Override
    public AbstractPacket getUiInfoResp(Role witness) {
        return new MonsterUiInfoResp(this.getObjectId(), this.getName(), this.getLifeAttr().getCurrentHp(), this.getLifeAttr().getMaxHp());
    }

    @Override
    public SceneObjectType getObjectType() {
        return SceneObjectType.MONSTER;
    }
}
