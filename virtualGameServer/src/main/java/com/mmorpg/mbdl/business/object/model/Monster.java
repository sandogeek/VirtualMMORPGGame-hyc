package com.mmorpg.mbdl.business.object.model;

import com.mmorpg.mbdl.business.object.packet.MonsterHpUpdate;
import com.mmorpg.mbdl.business.object.packet.MonsterUiInfoResp;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.business.role.model.prop.PropTree;
import com.mmorpg.mbdl.business.role.model.prop.PropType;
import com.mmorpg.mbdl.business.world.manager.SceneManager;
import com.mmorpg.mbdl.business.world.scene.model.Scene;
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
    public AbstractPacket getUiInfoResp(Role witness) {
        return new MonsterUiInfoResp(this.getObjectId(), this.getName(),
                propManager.getPropValueOf(PropType.CURRENT_HP),
                propManager.getPropValueOf(PropType.MAX_HP));
    }

    @Override
    public void init() {
        propManager.setPropTreeOnPropType(new PropTree() {
            @Override
            protected void doSetPropValue(long newValue) {
                super.doSetPropValue(newValue);
                Scene scene = SceneManager.getInstance().getSceneBySceneId(getSceneId());
                scene.broadcast(new MonsterHpUpdate(getObjectId(), newValue));
            }
        }, PropType.CURRENT_HP);
        propManager.getOrCreateTree(PropType.CURRENT_MP);
        propManager.setPropTreeOnPropType(new PropTree() {
            @Override
            protected void doSetPropValue(long newValue) {
                super.doSetPropValue(newValue);
                Scene scene = SceneManager.getInstance().getSceneBySceneId(getSceneId());
                scene.broadcast(new MonsterHpUpdate(getObjectId(), newValue));
            }
        }, PropType.MAX_HP);
        propManager.getOrCreateTree(PropType.MAX_MP);
        propManager.getOrCreateTree(PropType.ATTACK);
        propManager.getOrCreateTree(PropType.DEFENCE);
    }

    @Override
    public SceneObjectType getObjectType() {
        return SceneObjectType.MONSTER;
    }
}
