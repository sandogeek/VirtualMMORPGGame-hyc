package com.mmorpg.mbdl.business.role.model;

import com.google.common.base.MoreObjects;
import com.mmorpg.mbdl.business.object.model.AbstractCreature;
import com.mmorpg.mbdl.business.object.model.AbstractVisibleSceneObject;
import com.mmorpg.mbdl.business.object.model.SceneObjectType;
import com.mmorpg.mbdl.business.object.packet.RoleUiInfoResp;
import com.mmorpg.mbdl.business.role.entity.RoleEntity;
import com.mmorpg.mbdl.business.role.manager.RoleManager;
import com.mmorpg.mbdl.business.role.model.prop.PropType;
import com.mmorpg.mbdl.business.role.resource.RoleLevelRes;
import com.mmorpg.mbdl.business.world.manager.SceneManager;
import com.mmorpg.mbdl.business.world.scene.model.Scene;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ISession;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 角色
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public class Role extends AbstractCreature {
    private static Logger logger = LoggerFactory.getLogger(Role.class);
    private ISession session;

    /** 角色相关实体，所有会变更字段已自动mergeUpdate **/
    private RoleEntity roleEntity;

    public Role(Long objectId, String name) {
        super(objectId,name);
    }


    /**
     * 角色属性初始化
      */
    @Override
    public void init() {
        for (PropType propType :
                PropType.values()) {
            propManager.getOrCreateTree(propType);
        }
    }

    /**
     * init执行完毕后的初始化
     */
    public void afterInit() {
        RoleLevelRes roleLevelRes = RoleManager.getInstance().getRoleLevelResByLevel(roleEntity.getLevel());
        propManager.getPropTreeByType(PropType.MAX_HP).getOrCreateChild("level").set(roleLevelRes.getMaxHp());
        propManager.getPropTreeByType(PropType.MAX_MP).getOrCreateChild("level").set(roleLevelRes.getMaxMp());
        propManager.getPropTreeByType(PropType.ATTACK).getOrCreateChild("level").set(roleLevelRes.getAttack());
        propManager.getPropTreeByType(PropType.DEFENCE).getOrCreateChild("level").set(roleLevelRes.getDefence());
        propManager.setRootNodeValueOnType(PropType.SCENE_ID, roleEntity.getSceneId());
        propManager.setRootNodeValueOnType(PropType.EXP, roleEntity.getExp());
        fullHP();
        fullMP();
    }

    @Override
    public int getSceneId() {
        return (int)propManager.getPropValueOf(PropType.SCENE_ID);
    }

    @Override
    public AbstractPacket getUiInfoResp(Role witness) {
        return new RoleUiInfoResp()
                .setRoleId(roleEntity.getId())
                .setName(roleEntity.getName())
                .setLevel(roleEntity.getLevel())
                .setRoleType(roleEntity.getRoleType());
    }

    @Override
    public AbstractVisibleSceneObject setSceneId(int sceneId) {
        this.roleEntity.setSceneId(sceneId);
        return super.setSceneId(sceneId);
    }

    public RoleEntity getRoleEntity() {
        return roleEntity;
    }

    public Role setRoleEntity(RoleEntity roleEntity) {
        this.roleEntity = roleEntity;
        return this;
    }

    public Long getRoleId(){
        return getObjectId();
    }


    public ISession getSession() {
        return session;
    }

    public Role setSession(ISession session) {
        if (this.session == null){
            this.session = session;
        } else {
            logger.error("Role的session已设置，请勿重复设置");
        }
        return this;
    }

    public ChannelFuture sendPacket(AbstractPacket abstractPacket){
        return session.sendPacket(abstractPacket);
    }

    public ChannelFuture sendPacket(AbstractPacket abstractPacket,boolean flushNow){
        return session.sendPacket(abstractPacket,flushNow);
    }

    public void broadcastNotIncludeSelf(AbstractPacket abstractPacket) {
        broadcast(abstractPacket,false);
    }
    /**
     * 把消息广播给当前场景中的所有可见玩家
     * @param abstractPacket 要广播的包
     * @param includeSelf 是否包含自身
     */
    public void broadcast(AbstractPacket abstractPacket,boolean includeSelf){
        Scene scene = SceneManager.getInstance().getSceneBySceneId(getSceneId());
        if (includeSelf){
            for (Role role : scene.getObjId2Role().values()) {
                role.sendPacket(abstractPacket);
            }
        }else {
            scene.getObjId2Role().values().stream()
                    .filter(role -> role.equals(this)).forEach(role -> role.sendPacket(abstractPacket));
        }

    }
    @Override
    public SceneObjectType getObjectType() {
        return SceneObjectType.PLAYER;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("角色名称", roleEntity.getName())
                .toString();
    }
}
