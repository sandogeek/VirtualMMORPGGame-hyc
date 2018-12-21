package com.mmorpg.mbdl.bussiness.object.model;

import com.mmorpg.mbdl.bussiness.role.entity.RoleEntity;
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
        return null;
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


}
