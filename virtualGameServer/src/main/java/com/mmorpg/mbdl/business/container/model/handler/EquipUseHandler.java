package com.mmorpg.mbdl.business.container.model.handler;

import com.mmorpg.mbdl.business.container.model.Container;
import com.mmorpg.mbdl.business.container.model.ItemType;
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
    public boolean use(Role role, Container container) {
        return false;
    }
}
