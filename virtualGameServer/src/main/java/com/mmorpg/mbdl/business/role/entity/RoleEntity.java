package com.mmorpg.mbdl.business.role.entity;

import com.mmorpg.mbdl.common.orm.EnumReadableType;
import com.mmorpg.mbdl.business.role.model.RoleType;
import com.mmorpg.mbdl.framework.storage.annotation.JetCacheConfig;
import com.mmorpg.mbdl.framework.storage.core.AbstractEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * 角色简要信息实体
 *
 * @author Sando Geek
 * @since v1.0 2018/12/12
 **/
@Entity
@JetCacheConfig
@Table(indexes = {
        @Index(name = "index_account",columnList = "account"),
        @Index(name = "index_name_serverId",columnList = "NAME,serverToken",unique = true)
})
public class RoleEntity extends AbstractEntity<Long> {
    @Id
    @Column(nullable = false)
    private Long roleId;

    @Column(nullable = false)
    private String account;

    /**
     * 角色昵称
     */
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private short level = 0;
    @Column(nullable = false)
    private long exp = 0;
    @Column(nullable = false)
    private int serverToken;

    @Type(type = EnumReadableType.NAME)
    private RoleType roleType;

    @Column(nullable = false)
    private int sceneId;

    @Override
    public Long getId() {
        return roleId;
    }

    public int getSceneId() {
        return sceneId;
    }

    public RoleEntity setSceneId(int sceneId) {
        this.sceneId = sceneId;
        return this;
    }

    public RoleType getRoleType() {
        // if (roleType == null) {
        //     roleType = RoleType.getRoleTypeByCode(roleType);
        // }
        return roleType;
    }

    public RoleEntity setRoleId(Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public long getExp() {
        return exp;
    }

    public RoleEntity setExp(long exp) {
        this.exp = exp;
        return this;
    }

    public short getLevel() {
        return level;
    }

    public RoleEntity setLevel(short level) {
        this.level = level;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public RoleEntity setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getName() {
        return name;
    }

    public RoleEntity setName(String name) {
        this.name = name;
        return this;
    }

    public int getServerToken() {
        return serverToken;
    }

    public RoleEntity setServerToken(int serverToken) {
        this.serverToken = serverToken;
        return this;
    }

    public RoleEntity setRoleType(RoleType roleType) {
        this.roleType = roleType;
        return this;
    }

    // public RoleEntity setRoleTypeCode(byte roleType) {
    //     // this.roleType = roleType;
    //     return this;
    // }
}
