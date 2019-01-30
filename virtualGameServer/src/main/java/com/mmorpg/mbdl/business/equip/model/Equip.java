package com.mmorpg.mbdl.business.equip.model;

import com.google.common.base.MoreObjects;
import com.mmorpg.mbdl.business.container.manager.ContainerManager;
import com.mmorpg.mbdl.business.container.model.AbstractItem;
import com.mmorpg.mbdl.business.container.model.ItemType;

/**
 * 装备
 *
 * @author Sando Geek
 * @since v1.0 2019/1/30
 **/
public class Equip extends AbstractItem {
    private int currentDurability;

    public Equip() {
    }

    public Equip(int key, int currentDurability) {
        super(key, 1);
        ContainerManager.getInstance().getItemResByKey(key).getItemType()
        this.currentDurability = currentDurability;
    }

    @Override
    public ItemType getItemType() {
        return ItemType.EQUIP;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("currentDurability", currentDurability)
                .toString();
    }
}
