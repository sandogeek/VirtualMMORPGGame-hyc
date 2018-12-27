package com.mmorpg.mbdl.bussiness.world.scene.model;

import com.mmorpg.mbdl.bussiness.object.model.AbstractVisibleSceneObject;
import com.mmorpg.mbdl.bussiness.object.model.Role;
import com.mmorpg.mbdl.bussiness.world.manager.SceneManager;
import com.mmorpg.mbdl.bussiness.world.packet.ObjectDisappearResp;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 场景
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public class Scene {
    /**
     * 场景id
     */
    private int sceneId;
    /**
     * 场景名称
     */
    private String name;

    private Map<Long, AbstractVisibleSceneObject> objectId2VisibleObject = new ConcurrentHashMap<>(64);
    private Map<Long, Role> objId2Role = new ConcurrentHashMap<>(32);

    /**
     * 出现在场景中
     * @param visibleSceneObject 进入场景的可见物
     */
    public void appearInScene(AbstractVisibleSceneObject visibleSceneObject) {
        // 让场景内的其它玩家看到新出现的可见物
        objId2Role.values().forEach(role -> role.sendPacket(visibleSceneObject.getUiInfoResp(role)));
        // 让角色自身看到所有可见物
        if (visibleSceneObject instanceof Role) {
            Role self = (Role)visibleSceneObject;
            for (AbstractVisibleSceneObject visibleObject : objectId2VisibleObject.values()) {
                AbstractPacket uiInfoResp = visibleObject.getUiInfoResp(self);
                if (uiInfoResp== null) {
                    continue;
                }
                self.sendPacket(uiInfoResp);
            }
            self.sendPacket(SceneManager.getInstance().getSceneUiInfoResp(self));
            objId2Role.put(self.getRoleId(),self);
        }
        objectId2VisibleObject.put(visibleSceneObject.getObjectId(),visibleSceneObject);
    }

    /**
     * 在场景中消失
     * @param visibleSceneObject 消失在场景的可见物
     */
    public void disappearInScene(AbstractVisibleSceneObject visibleSceneObject) {
        // 玩家不需要看到自己消失，先移除自己再发包，避免下线时发包失败
        if (visibleSceneObject instanceof Role){
            Role role = (Role) visibleSceneObject;
            objId2Role.remove(role.getRoleId());
        }
        objId2Role.values().forEach(role -> role.sendPacket(new ObjectDisappearResp().setId(visibleSceneObject.getObjectId())));
        objectId2VisibleObject.remove(visibleSceneObject.getObjectId());
    }

    public int getSceneId() {
        return sceneId;
    }

    public Scene setSceneId(int sceneId) {
        this.sceneId = sceneId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Scene setName(String name) {
        this.name = name;
        return this;
    }
}
