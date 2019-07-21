package com.mmorpg.mbdl.framework.resource.impl;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;

import java.util.Map;

/**
 * 静态资源访问接口的实现类
 * TODO 实现getByUnique getByIndex功能
 * @author Sando Geek
 * @since v1.0
 **/
public abstract class StaticRes<K,V> implements IStaticRes<K,V> {
    /** 如果要热更，要注意并发访问带来的问题 */

    /** 资源文件全路径名 */
    private transient String fullFileName;

    /**
     * 缓存所有V,实现热更功能时需要记得把此值设置为null
     */
    private transient ImmutableList<V> values;

    @Override
    public V get(K key) {
        return get(key,true);
    }

    @Override
    public V get(K key, boolean throwExceptionNotExist) {
        V res = getKey2Resource().get(key);
        if (res == null && throwExceptionNotExist){
            throw new IllegalArgumentException(String.format("资源文件[%s]中不存在键为[%s]的静态资源",fullFileName,key));
        }
        return res;
    }

    @Override
    public boolean containsKey(K key) {
        return getKey2Resource().containsKey(key);
    }

    @Override
    public ImmutableList<V> values() {
        if (values != null) {
            return values;
        }
        values = ((ImmutableMap<K, V>) getKey2Resource()).values().asList();
        return values;
    }

    @Override
    public int size() {
        ImmutableCollection<V> vs = values != null ? values : ((ImmutableMap<K, V>) getKey2Resource()).values();
        return vs.size();
    }

    public abstract Map<K,V> getKey2Resource();

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }

}
