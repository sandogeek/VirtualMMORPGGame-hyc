package com.mmorpg.mbdl.business.object.model;

import com.mmorpg.mbdl.business.object.packet.MonsterUiInfoResp;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 怪物
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public class Monster extends AbstractCreature {
    @Override
    public AbstractPacket getUiInfoResp(Role witness) {
        return new MonsterUiInfoResp(this.getObjectId(), this.getName(), this.getCurrentHp(), this.getMaxHp());
    }

    @Override
    public SceneObjectType getObjectType() {
        return SceneObjectType.MONSTER;
    }
}
