package com.mmorpg.mbdl.business.container.model.creator;

import com.mmorpg.mbdl.business.container.model.AbstractItem;
import com.mmorpg.mbdl.business.container.model.ItemType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 抽象物品创建器
 *
 * @author Sando Geek
 * @since v1.0 2019/1/30
 **/
public abstract class AbstractItemCreator<T extends AbstractItem> {
    @Autowired
    private ItemCreatorManager itemCreatorManager;

    @PostConstruct
    public void register() {
        itemCreatorManager.register(this);
    }
    /**
     * 获取要创建的物品类型
     * @return
     */
    public abstract ItemType getItemType();

    /**
     * 创建物品逻辑
     * @return
     * @param key
     * @param amount
     */
    public abstract T create(int key, int amount);
}
