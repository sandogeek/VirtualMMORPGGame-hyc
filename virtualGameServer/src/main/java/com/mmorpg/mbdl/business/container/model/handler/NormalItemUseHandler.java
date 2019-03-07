package com.mmorpg.mbdl.business.container.model.handler;

import com.mmorpg.mbdl.business.container.manager.ContainerManager;
import com.mmorpg.mbdl.business.container.model.AbstractItem;
import com.mmorpg.mbdl.business.container.model.Container;
import com.mmorpg.mbdl.business.container.model.ItemType;
import com.mmorpg.mbdl.business.container.res.ItemRes;
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
    public boolean useById(Role role, Container packContainer, AbstractItem abstractItem, ItemRes itemRes, long objectId) {
        applyPropChange(role,itemRes.getPropChangeAfterUse());
        packContainer.removeItem(objectId, 1);
        ContainerManager.getInstance().mergeUpdateEntity(role.getContainerEntity());
        return false;
    }

    @Override
    public boolean useByKey(Role role, Container packContainer, int key, int amountToUse, ItemRes itemRes) {
        applyPropChange(role,itemRes.getPropChangeAfterUse());
        packContainer.removeItem(key, amountToUse);
        ContainerManager.getInstance().mergeUpdateEntity(role.getContainerEntity());
        return true;
    }
}
