package com.mmorpg.mbdl.business.container.model.handler;

import com.mmorpg.mbdl.business.container.model.Container;
import com.mmorpg.mbdl.business.container.model.ItemType;
import com.mmorpg.mbdl.business.role.model.Role;
import org.springframework.stereotype.Component;

/**
 * 常规物品使用处理器
 *
 * @author Sando Geek
 * @since v1.0 2019/2/15
 **/
@Component
public class NormalItemUseHandler extends AbstractItemUseHandler {
    @Override
    public ItemType getItemType() {
        return ItemType.NORMAL;
    }

    @Override
    public boolean use(Role role, Container container) {
        return false;
    }
}
