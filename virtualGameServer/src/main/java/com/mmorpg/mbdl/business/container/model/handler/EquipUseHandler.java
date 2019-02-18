package com.mmorpg.mbdl.business.container.model.handler;

import com.mmorpg.mbdl.business.container.manager.ContainerManager;
import com.mmorpg.mbdl.business.container.model.AbstractItem;
import com.mmorpg.mbdl.business.container.model.Container;
import com.mmorpg.mbdl.business.container.model.ItemType;
import com.mmorpg.mbdl.business.container.res.ItemRes;
import com.mmorpg.mbdl.business.equip.manager.EquipManager;
import com.mmorpg.mbdl.business.equip.model.Equip;
import com.mmorpg.mbdl.business.role.model.Role;
import org.springframework.stereotype.Component;

/**
 * 装备使用处理器
 *
 * @author Sando Geek
 * @since v1.0 2019/2/16
 **/
@Component
public class EquipUseHandler extends AbstractItemUseHandler {
    @Override
    public ItemType getItemType() {
        return ItemType.EQUIP;
    }

    @Override
    public boolean useById(Role role, Container packContainer, AbstractItem abstractItem, ItemRes itemRes, long objectId) {
        Equip oldEquip = EquipManager.getInstance().equip(role, (Equip) abstractItem);
        packContainer.removeItem(abstractItem.getObjectId(), 1);
        packContainer.addItem(oldEquip);
        ContainerManager.getInstance().mergeUpdateEntity(role.getContainerEntity());
        EquipManager.getInstance().mergeUpdateEntity(role.getEquipEntity());
        return true;
    }

    /**
     * 装备最大堆叠数必须为1，使用请求必须使用id
     *
     * @param role
     * @param packContainer
     * @param key
     * @param amount
     * @param itemRes
     * @return
     */
    @Override
    public boolean useByKey(Role role, Container packContainer, int key, int amount, ItemRes itemRes) {
        return false;
    }
}
