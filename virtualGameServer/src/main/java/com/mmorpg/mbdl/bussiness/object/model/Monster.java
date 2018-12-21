package com.mmorpg.mbdl.bussiness.object.model;

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
        return null;
    }
}
