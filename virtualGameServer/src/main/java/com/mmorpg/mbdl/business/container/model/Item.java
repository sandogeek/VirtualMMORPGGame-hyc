package com.mmorpg.mbdl.business.container.model;

import com.mmorpg.mbdl.framework.common.generator.IdGeneratorFactory;

/**
 * 容器中的物品
 *
 * @author Sando Geek
 * @since v1.0 2019/1/25
 **/
public class Item {
    /** 物品唯一id */
    private long objectId;
    /**
     * 对应物品表中的key
     */
    private int key;
    /**
     * 数量
     */
    private int amount;

    public Item() {
    }

    public Item(int key, int amount) {
        this.objectId = IdGeneratorFactory.getIntance().getObjectIdGenerator().generate();
        this.key = key;
        this.amount = amount;
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

    public int getAmount() {
        return amount;
    }

    public Item setAmount(int amount) {
        this.amount = amount;
        return this;
    }
}
