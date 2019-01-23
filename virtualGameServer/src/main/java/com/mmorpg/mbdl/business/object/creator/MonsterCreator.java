package com.mmorpg.mbdl.business.object.creator;

import com.mmorpg.mbdl.business.object.model.Monster;
import com.mmorpg.mbdl.business.object.model.SceneObjectType;
import com.mmorpg.mbdl.business.role.model.prop.PropType;
import com.mmorpg.mbdl.business.world.model.BornData;
import com.mmorpg.mbdl.business.world.resource.SceneObjectAttrRes;
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

        SceneObjectAttrRes sceneObjectAttrRes = this.sceneObjectAttrResMap.get(bornData.getObjectKey());
        Long id = IdGeneratorFactory.getIntance().getObjectIdGenerator().generate();
        Monster monster = new Monster(id,sceneObjectAttrRes.getName());
        monster.setSceneId(sceneId);
        monster.getPropManager().setRootNodeValueOnType(PropType.MAX_HP,sceneObjectAttrRes.getMaxHp());
        monster.getPropManager().setRootNodeValueOnType(PropType.MAX_MP,sceneObjectAttrRes.getMaxMp());
        monster.getPropManager().setRootNodeValueOnType(PropType.ATTACK,sceneObjectAttrRes.getAttack());
        monster.getPropManager().setRootNodeValueOnType(PropType.DEFENCE,sceneObjectAttrRes.getDefence());
        monster.fullHP();
        monster.fullMP();
        return monster;
    }
}
