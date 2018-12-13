package com.mmorpg.mbdl.bussiness.role.entity;

import com.mmorpg.mbdl.bussiness.role.model.RoleType;
import com.mmorpg.mbdl.framework.storage.annotation.JetCacheConfig;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 角色简要信息实体
 *
 * @author Sando Geek
 * @since v1.0 2018/12/12
 **/
@Entity
@JetCacheConfig
public class RoleEntity {
    @Id
    @Column(nullable = false)
    private Long roleId;
    @Column(nullable = false)
    private String account;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private byte roleType;
    @Transient
    private RoleType roleTypeEnum;
}
