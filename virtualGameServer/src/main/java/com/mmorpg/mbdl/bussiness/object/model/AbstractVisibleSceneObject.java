package com.mmorpg.mbdl.bussiness.object.model;

import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

/**
 * 可见物
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public abstract class AbstractVisibleSceneObject extends AbstractSceneObject {
    /**
     * 所在的场景id
     */
    private int sceneId;

    public int getSceneId() {
        return sceneId;
    }

    public AbstractVisibleSceneObject setSceneId(int sceneId) {
        this.sceneId = sceneId;
        return this;
    }

    /**
     * 获取该可见物提供给客户端的可见信息
     * @param witness 得到可见信息的角色
     * @return 可见信息响应包
     */
    public abstract AbstractPacket getUiInfoResp(Role witness);
}
