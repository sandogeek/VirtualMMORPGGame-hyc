package com.mmorpg.mbdl.bussiness.role.dao;

import com.mmorpg.mbdl.bussiness.role.entity.RoleEntity;
import com.mmorpg.mbdl.framework.storage.core.IStorage;

import java.util.List;

/**
 * 角色实体dao
 *
 * @author Sando Geek
 * @since v1.0 2018/12/14
 **/
public interface RoleEntityDao extends IStorage<Long, RoleEntity> {
    /**
     * 根据账号获取角色实体
     * @param account 账号
     * @return 角色实体列表
     */
    List<RoleEntity> findAllByAccount(String account);

    /**
     * 同服角色名称不能相同
     * @param name 角色名称
     * @param serverId 服务器Id
     * @return 角色实体列表
     */
    RoleEntity findByNameAndServerToken(String name,int serverId);
}