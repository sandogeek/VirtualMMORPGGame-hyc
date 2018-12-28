package com.mmorpg.mbdl.bussiness.object.creator;

import com.mmorpg.mbdl.bussiness.object.model.Monster;
import com.mmorpg.mbdl.bussiness.object.model.SceneObjectType;
import com.mmorpg.mbdl.bussiness.world.model.BornData;
import com.mmorpg.mbdl.bussiness.world.resource.SceneObjectAttrRes;
import com.mmorpg.mbdl.framework.common.generator.IdGeneratorFactory;
import org.springframework.stereotype.Component;

/**
 * 怪物生成器
 *
 * @author Sando Geek
 * @since v1.0 2018/12/28
 **/
@Component
public class MonsterCreator extends AbstractObjectCreator<Monster> {
    @Override
    public SceneObjectType getObjectType() {
        return SceneObjectType.MONSTER;
    }

    @Override
    public Monster create(int sceneId, BornData bornData) {
        Monster monster = new Monster();
        SceneObjectAttrRes sceneObjectAttrRes = this.sceneObjectAttrResMap.get(bornData.getObjectKey());
        monster.setCurrentMp(sceneObjectAttrRes.getMaxHp()).setMaxMp(sceneObjectAttrRes.getMaxHp())
                .setCurrentHp(sceneObjectAttrRes.getMaxMp()).setMaxHp(sceneObjectAttrRes.getMaxMp())
                .setAttack(sceneObjectAttrRes.getAttack())
                .setDefence(sceneObjectAttrRes.getDefence())
                .setSceneId(sceneId)
                .setName(sceneObjectAttrRes.getName())
                .setObjectId(IdGeneratorFactory.getIntance().getObjectIdGenerator().generate());
        return monster;
    }
}
