package com.mmorpg.mbdl.framework.storage.core;

import java.io.Serializable;

/**
 * 存储层接口
 * <p>带缓存功能的CRUD:</p>
 * @param <PK> 主键类型
 * @param <E> 实体类型
 */
public interface IStorage<PK extends Serializable&Comparable<PK>,E extends IEntity<PK>> {
    /**
     * 用entityBuilder创建一个实体，并将实体放入缓存和数据库
     * @param id 主键
     * @param entityCreator 实体创建器
     * @return 实体
     */
    E create(PK id, EntityCreator<PK,E> entityCreator);

    /**
     * 根据主键id获取一个实体（同步）
     * <p>先从缓存中找，找不到再从数据库中找</p>
     * @param id 主键
     * @return 返回IEntity对象，如果不存在返回null
     */
    E get(PK id);

    /**
     * 根据唯一字段（@Column(unique = true)的字段）获取一个实体（同步）
     * <p>先从缓存中找，找不到再从数据库中找</p>
     * @param name 字段名
     * @param value 字段值
     * @return 不存在则返回null
     */
    E getByUnique(String name,Object value);

    /**
     * 根据id获取实体，如果不存在就用entityBuilder创建一个实体，并将实体放入缓存（同步）和数据库（异步）
     * @param id 主键
     * @param entityCreator 实体创建器
     * @return 实体
     */
    E getOrCreate(PK id, EntityCreator<PK,E> entityCreator);

    /**
     * 更新缓存（同步）和数据库（异步）中的实体，如果不调用，缓存中的实体会根据策略同步到数据中<br>
     * 调用update是为了尽快保存重要变更，以免服务器故障导致数据丢失
     * @param entity
     * @return
     */
    E update(E entity);

    /**
     * 删除缓存（同步）和数据库（异步）中指定主键的实体
     * @param id 主键
     * @return 如果缓存中存在指定指定主键的实体，则返回相应的实体
     */
    E delete(PK id);

    /**
     * 使指定主键的缓存失效
     * @param id
     */
    void invalidate(PK id);

}
