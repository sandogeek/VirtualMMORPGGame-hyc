package com.mmorpg.mbdl.bussiness.object.creator;

import com.mmorpg.mbdl.bussiness.object.model.Npc;
import com.mmorpg.mbdl.bussiness.object.model.SceneObjectType;
import com.mmorpg.mbdl.bussiness.world.model.BornData;
import com.mmorpg.mbdl.bussiness.world.resource.SceneObjectAttrRes;
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
