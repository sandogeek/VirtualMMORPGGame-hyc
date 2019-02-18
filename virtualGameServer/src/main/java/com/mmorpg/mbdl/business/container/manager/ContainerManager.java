package com.mmorpg.mbdl.business.container.manager;

import com.mmorpg.mbdl.business.container.entity.ContainerEntity;
import com.mmorpg.mbdl.business.container.model.Container;
import com.mmorpg.mbdl.business.container.model.ContainerType;
import com.mmorpg.mbdl.business.container.res.ItemRes;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 容器管理器
 *
 * @author Sando Geek
 * @since v1.0 2019/1/24
 **/
@Component
public class ContainerManager {
    private static ContainerManager self;

    @Autowired
    private IStorage<Long, ContainerEntity> containerEntityIStorage;
    @Autowired
    private IStaticRes<Integer, ItemRes> itemResIStaticRes;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static ContainerManager getInstance() {
        return self;
    }

    public void bindContainerEntity(Role role) {
        ContainerEntity containerEntity = containerEntityIStorage.getOrCreate(role.getRoleId(), id -> {
            ContainerEntity entity = new ContainerEntity().setRoleId(id);
            // 赠送一点背包物品
            Container packContainer = new Container();
            // 放入5个小血瓶、5个小蓝瓶
            packContainer.createItem(1, 5);
            packContainer.createItem(2, 5);
            // 4中装备各发一把
            packContainer.createItem(10000, 1);
            packContainer.createItem(10001, 1);
            packContainer.createItem(10002, 1);
            packContainer.createItem(10003, 1);
            entity.getType2ContainerMap().put(ContainerType.PACK, packContainer);
            return entity;
        });
        role.setContainerEntity(containerEntity);
    }

    public ItemRes getItemResByKey(Integer key) {
        return itemResIStaticRes.get(key);
    }

    public void updateEntity(ContainerEntity containerEntity) {
        containerEntityIStorage.update(containerEntity);
    }

    public void mergeUpdateEntity(ContainerEntity containerEntity) {
        containerEntityIStorage.mergeUpdate(containerEntity);
    }
}
