package com.mmorpg.mbdl.business.container.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.MoreObjects;
import com.mmorpg.mbdl.business.container.manager.ContainerManager;
import com.mmorpg.mbdl.business.equip.model.Equip;
import com.mmorpg.mbdl.framework.common.generator.IdGeneratorFactory;

import java.util.Objects;

/**
 * 容器中的物品
 *
 * @author Sando Geek
 * @since v1.0 2019/1/25
 **/
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Equip.class),
        @JsonSubTypes.Type(value = NormalItem.class),
})
public abstract class AbstractItem implements Comparable<AbstractItem> {
    private long objectId;
    private int key;
    private int amount;

    public AbstractItem() {
    }

    public AbstractItem(int key, int amount) {
        this.key = key;
        this.amount = amount;
    }

    /**
     * 初始化新物品
     */
    AbstractItem init() {
        this.objectId = IdGeneratorFactory.getIntance().getObjectIdGenerator().generate();
        return this;
    }

    public abstract ItemType getItemType();

    public long getObjectId() {
        return objectId;
    }

    public AbstractItem setObjectId(long objectId) {
        this.objectId = objectId;
        return this;
    }

    public int getKey() {
        return key;
    }

    public AbstractItem setKey(int key) {
        this.key = key;
        return this;
    }

    @Override
    public int compareTo(AbstractItem other) {
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
        AbstractItem abstractItem = (AbstractItem) o;
        return objectId == abstractItem.objectId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId);
    }

    public int getAmount() {
        return amount;
    }

    public AbstractItem setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("objectId", objectId)
                .add("key", key)
                .add("name", ContainerManager.getInstance().getItemResByKey(key).getName())
                .add("amount", amount)
                .toString();
    }
}
