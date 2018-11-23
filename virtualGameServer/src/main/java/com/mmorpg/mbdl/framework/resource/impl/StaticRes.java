package com.mmorpg.mbdl.framework.resource.impl;

import com.google.common.collect.*;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;
import com.mmorpg.mbdl.framework.resource.facade.IStaticRes;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * 静态资源访问接口的实现类
 *
 * @author Sando Geek
 * @since v1.0
 **/
public class StaticRes<K,V> implements IStaticRes<K,V> {
    // TODO 热更实现，增加一个带热更功能的子类 implements Reloadable
    /** 包访问权限，方便ReflectASM设置值,如果要热更，要注意并发访问带来的问题 */
    ImmutableMap<K,V> key2Resource;
    /** 由于导表完成时Unique名称的数量和uniqueValue的数量都是确定的，所以底层使用ArrayTable */
    Table<String,Object,V> uniqueNameValue2Resource;
    Map<String,ImmutableListMultimap<Object,V>> indexNameKey2Resource;
    /** V的实际类型 */
    Class vClazz;

    @Override
    public V get(K key) {
        return get(key,true);
    }

    @Override
    public V get(K key, boolean throwExceptionNotExist) {
        V res = key2Resource.get(key);
        if (res == null && throwExceptionNotExist){
            ResDef resDef = (ResDef)vClazz.getAnnotation(ResDef.class);
            String tableName = StringUtils.isNotBlank(resDef.value())?resDef.value():vClazz.getSimpleName();
            throw new RuntimeException(String.format("表格[%s]中不存在键为[%s]的静态资源",tableName,key));
        }
        return res;
    }

    @Override
    public V getByUnique(String name, Object uniqueValue) {
        return uniqueNameValue2Resource.get(name,uniqueValue);
    }

    @Override
    public ImmutableList<V> getByIndex(String name, Object indexValue) {
        Optional<ImmutableListMultimap<Object,V>> optional = Optional.of(indexNameKey2Resource.get(name));
        return optional.map((value) -> value.get(indexValue)).orElse(null);
    }

    @Override
    public boolean containsKey(K key) {
        return key2Resource.containsKey(key);
    }

    @Override
    public ImmutableList<V> values() {
        return key2Resource.values().asList();
    }

    @Override
    public int size() {
        return key2Resource.size();
    }
}
