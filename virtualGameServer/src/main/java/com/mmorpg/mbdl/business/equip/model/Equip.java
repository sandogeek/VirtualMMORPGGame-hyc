package com.mmorpg.mbdl.business.equip.model;

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

    public Equip(int key, int amount, int currentDurability) {
        super(key, amount);
        this.currentDurability = currentDurability;
    }

    @Override
    public ItemType getItemType() {
        return ItemType.EQUIP;
    }

    public EquipType getEquipType() {
        return ContainerManager.getInstance().getItemResByKey(getKey()).getEquipType();
    }

    @Override
    public String toString() {
        return "Equip{" +
                "currentDurability=" + currentDurability +
                "} " + super.toString();
    }
}
