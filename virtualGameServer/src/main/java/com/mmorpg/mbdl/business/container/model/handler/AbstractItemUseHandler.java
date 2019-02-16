package com.mmorpg.mbdl.business.container.model.handler;

import com.mmorpg.mbdl.business.container.model.Container;
import com.mmorpg.mbdl.business.container.model.ItemType;
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
     * 使用物品
     * @param role 使用物品的角色
     * @param container 物品所在的容器
     * @return 使用成功返回true,否则返回false
     */
    public abstract boolean use(Role role, Container container);
}
