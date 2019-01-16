package com.mmorpg.mbdl.business.object.creator;

import com.mmorpg.mbdl.business.object.model.Npc;
import com.mmorpg.mbdl.business.object.model.SceneObjectType;
import com.mmorpg.mbdl.business.world.model.BornData;
import com.mmorpg.mbdl.business.world.resource.SceneObjectAttrRes;
import org.springframework.stereotype.Component;

/**
 * npc创建器
 *
 * @author Sando Geek
 * @since v1.0 2019/1/3
 **/
@Component
public class NpcCreator extends AbstractObjectCreator<Npc> {
    @Override
    public SceneObjectType getObjectType() {
        return SceneObjectType.NPC;
    }

    @Override
    public Npc create(int sceneId, BornData bornData) {
        SceneObjectAttrRes sceneObjectAttrRes = this.sceneObjectAttrResMap.get(bornData.getObjectKey());
        Npc npc = new Npc();
        npc.setSceneId(sceneId)
                .setName(sceneObjectAttrRes.getName())
                .setObjectId((long) bornData.getObjectKey());
        return npc;
    }
}
