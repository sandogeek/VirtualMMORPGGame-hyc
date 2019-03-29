package com.mmorpg.mbdl.common;

import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.framework.storage.core.AbstractEntity;

/**
 * 角色相关实体的管理器
 *
 * @author Sando Geek
 * @since v1.0 2019/2/1
 **/
public interface IRoleEntityManager<T extends AbstractEntity> {
    /**
     * 给特定的角色绑定实体
     * @param role 角色
     */
    void bindEntity(Role role);

    /**
     * 直接更新某个实体，一般在下线时使用
     */
    void updateEntity(T entity);

    /**
     * 合并更新某个实体一定时间内的变动,时间由实体类上的注解指定
     * @param entity
     */
    void mergeUpdateEntity(T entity);
}
