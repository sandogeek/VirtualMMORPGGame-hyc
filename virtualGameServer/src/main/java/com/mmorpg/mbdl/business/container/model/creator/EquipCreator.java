package com.mmorpg.mbdl.business.container.model.creator;

import com.mmorpg.mbdl.business.container.manager.ContainerManager;
import com.mmorpg.mbdl.business.container.model.ItemType;
import com.mmorpg.mbdl.business.container.res.ItemRes;
import com.mmorpg.mbdl.business.equip.model.Equip;
import org.springframework.stereotype.Component;

/**
 * 装备创建器
 *
 * @author Sando Geek
 * @since v1.0 2019/1/31
 **/
@Component
public class EquipCreator extends AbstractItemCreator<Equip> {
    @Override
    public ItemType getItemType() {
        return ItemType.EQUIP;
    }

    @Override
    public Equip create(int key, int amount) {
        ItemRes itemRes = ContainerManager.getInstance().getItemResByKey(key);
        return new Equip(key, amount, itemRes.getDurability());
    }
}
