package com.mmorpg.mbdl.framework.resource.exposed;

import com.google.common.collect.ImmutableList;

/**
 * 静态资源访问接口
 *
 * @author Sando Geek
 * @since v1.0
 **/
public interface IStaticRes<K,V> {

    /**
     * 根据键获取V类型的对象，相当于调用get(K key,true)
     * @param key 资源键的值
     * @return
     * @throws IllegalArgumentException 如果资源不存在
     */
    V get(K key);

    /**
     * 根据键获取V类型的对象
     * @param key 资源主键值
     * @param throwExceptionNotExist 资源不存在时是否抛出异常
     * @return if存在，资源对象 if不存在 if throwExceptionNotExist==true else null
     * @throws IllegalArgumentException throwExceptionNotExist==true 并且资源不存在
     */
    V get(K key,boolean throwExceptionNotExist);

    /**
     * 当前key是否有对应的值
     * @param key
     * @return 有 true 没有 false
     */
    boolean containsKey(K key);

    /**
     * 获取全部静态资源对象
     * @return 装载静态资源对象的ImmutableList,注意，不会返回null,如果没有对应的资源对象，返回size()为0的ImmutableList
     */
    ImmutableList<V> values();

    /**
     * 资源对象的数量
     * @return 资源对象的数量
     */
    int size();
}
