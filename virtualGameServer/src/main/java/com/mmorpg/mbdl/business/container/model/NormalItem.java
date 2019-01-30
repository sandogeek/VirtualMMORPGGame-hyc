package com.mmorpg.mbdl.business.container.model;

/**
 * 常规物品
 *
 * @author Sando Geek
 * @since v1.0 2019/1/30
 **/
public class NormalItem extends AbstractItem {
    @Override
    public ItemType getItemType() {
        return ItemType.NORMAL;
    }
}
