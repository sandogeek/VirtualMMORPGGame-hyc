package com.mmorpg.mbdl.business.container.model.creator;

import com.mmorpg.mbdl.business.container.model.ItemType;
import com.mmorpg.mbdl.business.container.model.NormalItem;
import org.springframework.stereotype.Component;

/**
 * 常规物品创建器
 *
 * @author Sando Geek
 * @since v1.0 2019/1/31
 **/
@Component
public class NormalItemCreator extends AbstractItemCreator<NormalItem> {
    @Override
    public ItemType getItemType() {
        return ItemType.NORMAL;
    }

    @Override
    public NormalItem create(int key, int amount) {
        return new NormalItem(key,amount);
    }
}
