package com.mmorpg.mbdl.framework.resource.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;
import com.mmorpg.mbdl.framework.resource.facade.IStaticRes;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * 静态资源访问接口的实现类
 * TODO 实现getByUnique getByIndex功能
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
        V res = Optional.ofNullable(key2Resource).map((value) -> value.get(key)).orElse(null);
        if (res == null && throwExceptionNotExist){
            ResDef resDef = (ResDef)vClazz.getAnnotation(ResDef.class);
            String suffix = resDef.getSuffix();
            String[] fileNamesWithoutSuffix = resDef.value();
            // 先把空字符串替换为null,否则空字符串join后可能变为",,",isNotBlank失去作用
            for (int i = 0; i < fileNamesWithoutSuffix.length; i++) {
                if ("".equals(fileNamesWithoutSuffix[i])){
                    fileNamesWithoutSuffix[i] = null;
                }
            }
            String resources = StringUtils.join(fileNamesWithoutSuffix, ",");
            String tableName = StringUtils.isNotBlank(resources)?resources:vClazz.getSimpleName();
            throw new RuntimeException(String.format("资源文件[%s,suffix=%s]中不存在键为[%s]的静态资源",tableName,suffix,key));
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
        return Optional.ofNullable(key2Resource).map(value -> value.containsKey(key)).orElse(false);
    }

    @Override
    public ImmutableList<V> values() {
        ImmutableList<V> result = Optional.ofNullable(key2Resource).map(value -> value.values().asList()).orElse(null);
        if (result == null){
            result = Optional.ofNullable(uniqueNameValue2Resource).map(value -> ImmutableList.copyOf(value.values())).orElse(null);
        }else if (result==null){
            result = Optional.ofNullable(indexNameKey2Resource).map(value -> {
                ImmutableList.Builder<V> builder = ImmutableList.builder();
                value.values().forEach((vImmutableListMultimap) -> {
                     builder.addAll(vImmutableListMultimap.values());
                 });
                 return builder.build();
            }).orElse(null);
        }
        if (result==null){
            ImmutableList.Builder<V> builder = ImmutableList.builder();
            result = builder.build();
        }
        return result;
    }

    @Override
    public int size() {
        return values().size();
    }
}
