package com.mmorpg.mbdl.business.container.model.handler;

import com.mmorpg.mbdl.business.container.model.AbstractItem;
import com.mmorpg.mbdl.business.container.model.Container;
import com.mmorpg.mbdl.business.container.model.ItemType;
import com.mmorpg.mbdl.business.container.res.ItemRes;
import com.mmorpg.mbdl.business.role.model.Role;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 抽象物品使用处理器
 *
 * @author Sando Geek
 * @since v1.0 2019/2/15
 **/
public abstract class AbstractItemUseHandler {
    @Autowired
    private ItemUseHandlerManager itemUseHandlerManager;

    @PostConstruct
    public void register() {
        itemUseHandlerManager.register(this);
    }

    /**
     * 获取使用的物品的类型
     * @return
     */
    public abstract ItemType getItemType();

    /**
     * 使用最大堆叠数为1的物品时调用（此时请求传入的是objectId）
     * @param role 使用物品的角色
     * @param packContainer
     * @param abstractItem
     * @param itemRes
     * @param objectId
     * @return 使用成功返回true,否则返回false
     */
    public abstract boolean useById(Role role, Container packContainer, AbstractItem abstractItem, ItemRes itemRes, long objectId);

    /**
     * 使用最大堆叠数不为1的物品时调用（此时请求传入的是key）
     */
    public abstract boolean useByKey(Role role, Container packContainer, int key, int amount, ItemRes itemRes);
}
