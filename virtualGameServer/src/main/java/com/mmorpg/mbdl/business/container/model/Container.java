package com.mmorpg.mbdl.business.container.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import com.mmorpg.mbdl.business.container.manager.ContainerManager;
import com.mmorpg.mbdl.business.container.res.ItemRes;
import com.mmorpg.mbdl.framework.common.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;

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
    private TreeMultimap<Integer, Item> key2ItemMultiMap = TreeMultimap.create();

    /**
     * 添加物品
     * @param key 配置表中的
     * @param amount 数量
     * @return 添加成功返回true，失败返回false
     */
    public boolean addItem(int key, int amount) {
        ItemRes itemRes = ContainerManager.getInstance().getItemResByKey(key);
        int maxAmount = itemRes.getMaxAmount();
        if (maxAmount == 1) {
            for (int i = 0; i < amount; i++) {
                doAddItem(new Item(key,1));
            }
            return true;
        }
        if (itemRes == null) {
            return false;
        }
        Item item = new Item(key, amount);
        // 找到id最新的同类物品，判断其是否达到最大堆叠数，没达到就把物品放到这个物品上
        NavigableSet<Item> items = key2ItemMultiMap.get(item.getKey());
        if (!items.isEmpty()) {
            // 自动排序了，最后一个是id最大（最新）的
            Item lastItem = Iterables.getLast(items);
            // 最后一个物品的剩余空间
            int amountLeft = maxAmount - lastItem.getAmount();
            // 还可以继续放入
            if (amountLeft > 0) {
                // 判断物品剩余空间能不能完全容纳此物品
                if (amountLeft >= item.getAmount()) {
                    // 可以完全容下，直接变更最新物品的数量即可
                    lastItem.setAmount(lastItem.getAmount() + item.getAmount());
                } else {
                    lastItem.setAmount(maxAmount);
                    addItem(key, amount - amountLeft);
                }
            } else {
                addItemHelper(key, amount, item, maxAmount);
            }
        } else {
            addItemHelper(key, amount, item, maxAmount);
        }
        return true;
    }

    private void addItemHelper(int key, int amount, Item item, int maxAmount) {
        if (amount > maxAmount) {
            item.setAmount(maxAmount);
            doAddItem(item);
            addItem(key,amount - maxAmount);
        } else {
            doAddItem(item);
        }
    }

    private void doAddItem(Item item) {
        item.init();
        id2ItemMap.put(item.getObjectId(), item);
        key2ItemMultiMap.put(item.getKey(), item);
    }

    /**
     * 删除物品
     * @param key 物品配置表key
     * @param amount 删除数量
     */
    public void removeItem(int key, int amount) {
        Item item = id2ItemMap.get(key);
        if (item == null) {
            return;
        }
        NavigableSet<Item> items = key2ItemMultiMap.get(item.getKey());
        Item lastItem = Iterables.getLast(items);
        int lack = lastItem.getAmount() - amount;
        // 最后一个物品够用
        if (lack > 0) {
            lastItem.setAmount(lack);
            return;
        } else if (lack == 0) {
            doRemoveItem(lastItem);
        } else {
            // 不够用
            doRemoveItem(lastItem);
            removeItem(key,-lack);
        }

    }

    private void doRemoveItem(Item item) {
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id2ItemMap", JsonUtil.object2String(id2ItemMap))
                .toString();
    }
}
