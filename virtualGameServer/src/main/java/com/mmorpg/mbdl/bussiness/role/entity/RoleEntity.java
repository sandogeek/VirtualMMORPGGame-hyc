package com.mmorpg.mbdl.bussiness.role.entity;

import com.mmorpg.mbdl.bussiness.role.model.RoleType;
import com.mmorpg.mbdl.framework.storage.annotation.JetCacheConfig;
import com.mmorpg.mbdl.framework.storage.core.IEntity;

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
        @Index(name = "index_name_serverId",columnList = "name,serverToken",unique = true)
})
public class RoleEntity implements IEntity<Long> {
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
    private int serverToken;

    @Column(nullable = false,columnDefinition = "tinyint(1)")
    private byte roleTypeCode;
    @Transient
    private RoleType roleType;

    @Override
    public Long getId() {
        return roleId;
    }

    public RoleType getRoleType() {
        if (roleType == null) {
            roleType = RoleType.getRoleTypeByCode(roleTypeCode);
        }
        return roleType;
    }

    public RoleEntity setRoleId(Long roleId) {
        this.roleId = roleId;
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

    public byte getRoleTypeCode() {
        return roleTypeCode;
    }

    public RoleEntity setRoleTypeCode(byte roleTypeCode) {
        this.roleTypeCode = roleTypeCode;
        return this;
    }
}
