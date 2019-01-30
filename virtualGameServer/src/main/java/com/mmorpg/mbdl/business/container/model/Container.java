package com.mmorpg.mbdl.business.container.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import com.google.common.collect.TreeMultimap;
import com.mmorpg.mbdl.business.container.exception.ItemNotEnoughException;
import com.mmorpg.mbdl.business.container.manager.ContainerManager;
import com.mmorpg.mbdl.business.container.res.ItemRes;
import com.mmorpg.mbdl.framework.common.utils.JsonUtil;

import java.util.Collection;
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
    private Map<Long, AbstractItem> id2ItemMap = new HashMap<>();
    /**
     * 内部维护的用于合并可合并物品的数据结构
     */
    @JsonIgnore
    private TreeMultimap<Integer, AbstractItem> key2ItemMultiMap = TreeMultimap.create();

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
                doAddItem(new AbstractItem(key,1));
            }
            return true;
        }
        addItemHelper1(key, amount, maxAmount);
        return true;
    }

    private void addItemHelper1(int key, int amount, int maxAmount) {
        AbstractItem abstractItem = new AbstractItem(key, amount);
        // 找到id最新的同类物品，判断其是否达到最大堆叠数，没达到就把物品放到这个物品上
        NavigableSet<AbstractItem> abstractItems = key2ItemMultiMap.get(abstractItem.getKey());
        if (!abstractItems.isEmpty()) {
            // 自动排序了，最后一个是id最大（最新）的
            AbstractItem lastAbstractItem = Iterables.getLast(abstractItems);
            // 最后一个物品的剩余空间
            int amountLeft = maxAmount - lastAbstractItem.getAmount();
            // 还可以继续放入
            if (amountLeft > 0) {
                // 判断物品剩余空间能不能完全容纳此物品
                if (amountLeft >= abstractItem.getAmount()) {
                    // 可以完全容下，直接变更最新物品的数量即可
                    lastAbstractItem.setAmount(lastAbstractItem.getAmount() + abstractItem.getAmount());
                } else {
                    lastAbstractItem.setAmount(maxAmount);
                    addItemHelper1(key, amount - amountLeft,maxAmount);
                }
            } else {
                addItemHelper2(key, amount, abstractItem, maxAmount);
            }
        } else {
            addItemHelper2(key, amount, abstractItem, maxAmount);
        }
    }

    private void addItemHelper2(int key, int amount, AbstractItem abstractItem, int maxAmount) {
        if (amount > maxAmount) {
            abstractItem.setAmount(maxAmount);
            doAddItem(abstractItem);
            addItemHelper1(key,amount - maxAmount,maxAmount);
        } else {
            doAddItem(abstractItem);
        }
    }

    private void doAddItem(AbstractItem abstractItem) {
        abstractItem.init();
        id2ItemMap.put(abstractItem.getObjectId(), abstractItem);
        key2ItemMultiMap.put(abstractItem.getKey(), abstractItem);
    }

    /**
     * 根据配置表key，删除物品，通常可堆叠的物品属性是一致的，有随机属性的物品不可堆叠，所以这个函数只能用在可堆叠物品上
     * @param key 物品配置表key
     * @param amount 删除数量
     * @exception com.mmorpg.mbdl.business.container.exception.ItemNotEnoughException 物品数量不足
     */
    public boolean removeItem(int key, int amount) {
        ItemRes itemRes = ContainerManager.getInstance().getItemResByKey(key);
        int maxAmount = itemRes.getMaxAmount();
        if (maxAmount == 1) {
            throw new RuntimeException("不可堆叠物品（上限为1）不可使用此函数扣除");
        }
        NavigableSet<AbstractItem> abstractItems = key2ItemMultiMap.get(key);
        if (abstractItems.isEmpty()) {
            throw new ItemNotEnoughException("物品数量不足");
        }
        Integer sum = abstractItems.stream().map(AbstractItem::getAmount).reduce(0, Integer::sum);
        if (amount > sum) {
            throw new ItemNotEnoughException("物品数量不足");
        }
        removeHelper(key,amount, abstractItems);
        return true;
    }

    private void removeHelper(int key, int amount, NavigableSet<AbstractItem> abstractItems) {
        AbstractItem lastAbstractItem = Iterables.getLast(abstractItems);
        int lack = lastAbstractItem.getAmount() - amount;
        // 最后一个物品够用
        if (lack > 0) {
            lastAbstractItem.setAmount(lack);
        } else if (lack == 0) {
            doRemoveItem(lastAbstractItem);
        } else {
            // 不够用
            doRemoveItem(lastAbstractItem);
            removeHelper(key,-lack,key2ItemMultiMap.get(key));
        }
    }

    public boolean removeItem(long objectId, int amount) {
        AbstractItem abstractItem = id2ItemMap.get(objectId);
        if (abstractItem == null) {
            return false;
        }
        ItemRes itemRes = ContainerManager.getInstance().getItemResByKey(abstractItem.getKey());
        int maxAmount = itemRes.getMaxAmount();
        if (maxAmount == 1) {
            doRemoveItem(abstractItem);
        } else {
            removeItem(abstractItem.getKey(),amount);
        }
        return true;
    }

    private void doRemoveItem(AbstractItem abstractItem) {
        id2ItemMap.remove(abstractItem.getObjectId());
        key2ItemMultiMap.remove(abstractItem.getKey(), abstractItem);
    }

    public Collection<AbstractItem> getAll() {
        return id2ItemMap.values();
    }

    /**
     * jackson反序列化时会调用以初始化 key2ItemMultiMap
     * @param id2ItemMap
     * @return
     */
    public Container setId2ItemMap(Map<Long, AbstractItem> id2ItemMap) {
        id2ItemMap.values().forEach(item -> key2ItemMultiMap.put(item.getKey(), item));
        this.id2ItemMap = id2ItemMap;
        return this;
    }

    public AbstractItem getItemByObjectId(long objectId) {
        return id2ItemMap.get(objectId);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id2ItemMap", JsonUtil.object2String(id2ItemMap))
                .toString();
    }
}
