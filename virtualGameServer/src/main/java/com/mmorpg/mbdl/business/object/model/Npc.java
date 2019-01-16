package com.mmorpg.mbdl.business.object.model;

import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * npc
 *
 * @author Sando Geek
 * @since v1.0 2019/1/3
 **/
public class Npc extends AbstractVisibleSceneObject {
    @Override
    public AbstractPacket getUiInfoResp(Role witness) {
        return null;
    }

    @Override
    public SceneObjectType getObjectType() {
        return SceneObjectType.NPC;
    }
}
