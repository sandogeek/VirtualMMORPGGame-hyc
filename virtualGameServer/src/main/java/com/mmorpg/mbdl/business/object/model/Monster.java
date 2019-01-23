package com.mmorpg.mbdl.business.object.model;

import com.mmorpg.mbdl.business.object.packet.MonsterUiInfoResp;
import com.mmorpg.mbdl.business.role.model.prop.PropType;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 怪物
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public class Monster extends AbstractCreature {


    public Monster(Long objectId, String name) {
        super(objectId, name);
    }

    @Override
    protected void init() {

    }

    @Override
    public AbstractPacket getUiInfoResp(Role witness) {
        return new MonsterUiInfoResp(this.getObjectId(), this.getName(),
                propManager.getPropValueOf(PropType.CURRENT_HP),
                propManager.getPropValueOf(PropType.MAX_HP));
    }

    @Override
    public SceneObjectType getObjectType() {
        return SceneObjectType.MONSTER;
    }
}
