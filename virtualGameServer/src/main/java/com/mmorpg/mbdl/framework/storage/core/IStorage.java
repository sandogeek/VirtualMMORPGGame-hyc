package com.mmorpg.mbdl.framework.storage.core;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * 存储层（包括缓存）接口
 * 参考：https://stackoverflow.com/questions/42133023/is-there-a-way-to-register-a-repository-base-class-with-a-spring-boot-auto-confi
 * https://es.yemengying.com/4/4.6/4.6.2.html
 * <p>带缓存功能的CRUD:</p>
 * @param <PK> 主键类型
 * @param <E> 实体类型
 * @author sando
 */
@NoRepositoryBean
public interface IStorage<PK extends Serializable&Comparable<PK>,E extends AbstractEntity<PK>> extends Repository<E, PK> {
    /**
     * 用jpa创建一个实体，并将实体放入缓存和数据库
     * @param entity 需要创建的实体
     * @return 实体
     */
    E create(E entity);

    /**
     * 根据主键id获取一个实体（同步）
     * <p>先从缓存中找，找不到再从数据库中找</p>
     * @param id 主键
     * @return 返回IEntity对象，如果不存在返回null
     */
    E get(PK id);

    // /**
    //  * 根据唯一字段（@Column(unique = true)的字段）获取一个实体（同步）
    //  * <p>先从缓存中找，找不到再从数据库中找</p>
    //  * @param name 字段名
    //  * @param value 字段值
    //  * @return 不存在则返回null
    //  */
    // E getByUnique(String name,Object value);

    /**
     * 根据id获取实体，如果不存在就用entityBuilder创建一个实体，并将实体放入缓存（同步）和数据库（异步）
     * @param id 主键
     * @param entityCreator 实体创建器
     * @return 实体
     */
    E getOrCreate(PK id, EntityCreator<PK,E> entityCreator);

    /**
     * 更新缓存（同步）和数据库（异步）中的实体<br>
     * 调用update是为了尽快保存重要变更，以免服务器故障导致数据丢失
     * @param entity
     * @return
     */
    E update(E entity);

    /**
     * 根据实体类注解上的delay，把所有delay时间内的更新合并
     * @param entity 实体
     *
     */
    void mergeUpdate(E entity);

    /**
     * 删除缓存（同步）和数据库（异步）中指定主键的实体
     * @param id 主键
     * @return 如果存在指定主键的实体，则返回相应的实体,否则返回null
     */
    E remove(PK id);

    // /**
    //  * 使指定主键的缓存失效
    //  * @param id
    //  */
    // void invalidate(PK id);

}
