package com.mmorpg.mbdl.framework.resource.impl;

import com.google.common.collect.*;

import java.util.Map;
import java.util.Optional;

/**
 * 静态资源访问接口的实现类
 * TODO 实现getByUnique getByIndex功能
 * @author Sando Geek
 * @since v1.0
 **/
public class StaticRes<K,V> {
    // TODO 增量热更（只热更变更的行）实现（暂时没思路，目前普通热更），增加一个带热更功能的子类 implements Reloadable
    /** 如果要热更，要注意并发访问带来的问题 */
    private ImmutableMap<K,V> key2Resource;
    /** 由于导表完成时Unique名称的数量和uniqueValue的数量都是确定的，所以底层使用ArrayTable */
    private Table<String,Object,V> uniqueNameValue2Resource;
    private Map<String,ImmutableListMultimap<Object,V>> indexNameKey2Resource;
    // /** V的实际类型 */
    // Class vClazz;
    /** 资源文件全路径名 */
    private String fullFileName;

    /**
     * 缓存所有V,实现热更功能时需要记得把此值设置为null
     */
    private ImmutableList<V> values;

    public V get(K key) {
        return get(key,true);
    }

    public V get(K key, boolean throwExceptionNotExist) {
        V res = Optional.of(key2Resource).map((value) -> value.get(key)).orElse(null);
        if (res == null && throwExceptionNotExist){
            throw new RuntimeException(String.format("资源文件[%s]中不存在键为[%s]的静态资源",fullFileName,key));
        }
        return res;
    }

    public V getByUnique(String name, Object uniqueValue) {
        return Optional.ofNullable(uniqueNameValue2Resource).map(value-> value.get(name,uniqueValue)).orElse(null);
    }

    public ImmutableList<V> getByIndex(String name, Object indexValue) {
        Optional<ImmutableListMultimap<Object,V>> optional = Optional.ofNullable(indexNameKey2Resource).map(value -> value.get(name));
        return optional.map((value) -> value.get(indexValue)).orElse(null);
    }

    public boolean containsKey(K key) {
        return key2Resource.containsKey(key);
    }

    public ImmutableList<V> values() {
        if (values != null) {
            return values;
        }
        values = key2Resource.values().asList();
        return values;
    }

    public int size() {
        ImmutableCollection<V> vs = values != null ? values : key2Resource.values();
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
