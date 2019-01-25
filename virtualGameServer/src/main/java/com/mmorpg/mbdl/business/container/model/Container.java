package com.mmorpg.mbdl.business.container.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mmorpg.mbdl.framework.common.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 容器
 *
 * @author Sando Geek
 * @since v1.0 2019/1/15
 **/
public class Container {
    private Map<Long, Item> id2ItemMap = new HashMap<>();
    /**
     * 内部维护的用于合并可合并物品的数据结构
     */
    @JsonIgnore
    private Multimap<Integer, Item> key2ItemMultiMap = ArrayListMultimap.create();

    /**
     * 添加物品
     * @param item
     */
    public void addItem(Item item) {
        id2ItemMap.put(item.getObjectId(), item);
        key2ItemMultiMap.put(item.getKey(), item);
    }

    public void removeItem(Item item) {
        id2ItemMap.remove(item.getObjectId());
        key2ItemMultiMap.remove(item.getKey(),item);
    }

    public Map<Long, Item> getId2ItemMap() {
        return id2ItemMap;
    }

    public Container setId2ItemMap(Map<Long, Item> id2ItemMap) {
        id2ItemMap.values().forEach(item -> key2ItemMultiMap.put(item.getKey(), item));
        this.id2ItemMap = id2ItemMap;
        return this;
    }

    public Multimap<Integer, Item> getKey2ItemMultiMap() {
        return key2ItemMultiMap;
    }

    public Container setKey2ItemMultiMap(Multimap<Integer, Item> key2ItemMultiMap) {
        this.key2ItemMultiMap = key2ItemMultiMap;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id2ItemMap", JsonUtil.object2String(id2ItemMap))
                .toString();
    }
}
