package com.mmorpg.mbdl.business.container.model;

import com.google.common.base.MoreObjects;
import com.mmorpg.mbdl.framework.common.generator.IdGeneratorFactory;

import java.util.Objects;

/**
 * 容器中的物品
 *
 * @author Sando Geek
 * @since v1.0 2019/1/25
 **/
public class Item implements Comparable<Item> {
    private long objectId;
    private int key;
    private int amount;

    public Item() {
    }

    public Item(int key, int amount) {
        this.key = key;
        this.amount = amount;
    }

    /**
     * 初始化新物品
     */
    Item init() {
        this.objectId = IdGeneratorFactory.getIntance().getObjectIdGenerator().generate();
        return this;
    }

    public long getObjectId() {
        return objectId;
    }

    public Item setObjectId(long objectId) {
        this.objectId = objectId;
        return this;
    }

    public int getKey() {
        return key;
    }

    public Item setKey(int key) {
        this.key = key;
        return this;
    }

    @Override
    public int compareTo(Item other) {
        if (this.objectId < other.objectId) {
            return -1;
        } else if (this.objectId > other.objectId) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return objectId == item.objectId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId);
    }

    public int getAmount() {
        return amount;
    }

    public Item setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("objectId", objectId)
                .add("key", key)
                .add("amount", amount)
                .toString();
    }
}
