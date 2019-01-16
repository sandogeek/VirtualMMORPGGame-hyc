package com.mmorpg.mbdl.business.world.scene.packet.vo;

import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

/**
 * 可前往的场景信息
 *
 * @author Sando Geek
 * @since v1.0 2018/12/26
 **/
public class SceneCanGoInfo {
    @Protobuf(description = "场景id",required = true)
    private int sceneId;
    /**
     * TODO 前端也能解析静态资源，从而节省掉这个字段
     */
    @Protobuf(description = "场景名称",required = true)
    private String sceneName;

    public SceneCanGoInfo setSceneId(int sceneId) {
        this.sceneId = sceneId;
        return this;
    }

    public SceneCanGoInfo setSceneName(String sceneName) {
        this.sceneName = sceneName;
        return this;
    }
}
