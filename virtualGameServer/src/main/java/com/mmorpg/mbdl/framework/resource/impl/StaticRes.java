package com.mmorpg.mbdl.framework.resource.impl;

import com.google.common.collect.*;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;

import java.util.Map;
import java.util.Optional;

/**
 * 静态资源访问接口的实现类
 * TODO 实现getByUnique getByIndex功能
 * @author Sando Geek
 * @since v1.0
 **/
public class StaticRes<K,V> implements IStaticRes<K,V> {
    // TODO 增量热更（只热更变更的行）实现（暂时没思路，目前普通热更），增加一个带热更功能的子类 implements Reloadable
    /** 如果要热更，要注意并发访问带来的问题 */
    private Map<K,V> key2Resource;
    /** 由于导表完成时Unique名称的数量和uniqueValue的数量都是确定的，所以底层使用ArrayTable */
    private Table<String,Object,V> uniqueNameValue2Resource;
    private Map<String,ImmutableListMultimap<Object,V>> indexNameKey2Resource;
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
        V res = key2Resource.get(key);
        if (res == null && throwExceptionNotExist){
            throw new IllegalArgumentException(String.format("资源文件[%s]中不存在键为[%s]的静态资源",fullFileName,key));
        }
        return res;
    }

    @Override
    public V getByUnique(String name, Object uniqueValue) {
        return Optional.ofNullable(uniqueNameValue2Resource).map(value-> value.get(name,uniqueValue)).orElse(null);
    }

    @Override
    public ImmutableList<V> getByIndex(String name, Object indexValue) {
        Optional<ImmutableListMultimap<Object,V>> optional = Optional.ofNullable(indexNameKey2Resource).map(value -> value.get(name));
        return optional.map((value) -> value.get(indexValue)).orElse(null);
    }

    @Override
    public boolean containsKey(K key) {
        return key2Resource.containsKey(key);
    }

    @Override
    public ImmutableList<V> values() {
        if (values != null) {
            return values;
        }
        values = ((ImmutableMap<K, V>) key2Resource).values().asList();
        return values;
    }

    @Override
    public int size() {
        ImmutableCollection<V> vs = values != null ? values : ((ImmutableMap<K, V>) key2Resource).values();
        return vs.size();
    }

    public void setFullFileName(String fullFileName) {
        this.fullFileName = fullFileName;
    }

    public void setKey2Resource(ImmutableMap<K, V> key2Resource) {
        this.key2Resource = key2Resource;
    }

    public void setUniqueNameValue2Resource(Table<String, Object, V> uniqueNameValue2Resource) {
        this.uniqueNameValue2Resource = uniqueNameValue2Resource;
    }

    public void setIndexNameKey2Resource(Map<String, ImmutableListMultimap<Object, V>> indexNameKey2Resource) {
        this.indexNameKey2Resource = indexNameKey2Resource;
    }
}
