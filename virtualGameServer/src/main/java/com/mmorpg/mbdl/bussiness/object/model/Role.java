package com.mmorpg.mbdl.bussiness.object.model;

import com.mmorpg.mbdl.bussiness.object.packet.RoleUiInfoResp;
import com.mmorpg.mbdl.bussiness.role.entity.RoleEntity;
import com.mmorpg.mbdl.bussiness.world.manager.SceneManager;
import com.mmorpg.mbdl.bussiness.world.scene.model.Scene;
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
    private RoleEntity roleEntity;

    @Override
    public AbstractPacket getUiInfoResp(Role witness) {
        return new RoleUiInfoResp()
                .setRoleId(roleEntity.getId())
                .setName(roleEntity.getName())
                .setLevel(roleEntity.getLevel())
                .setRoleType(roleEntity.getRoleType());
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

    public Role setRoleId(Long id){
        setObjectId(id);
        return this;
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
     * 把消息广播给所有可见玩家
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
}
