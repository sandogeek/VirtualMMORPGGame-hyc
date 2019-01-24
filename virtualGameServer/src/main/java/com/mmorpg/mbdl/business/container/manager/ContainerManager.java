package com.mmorpg.mbdl.business.container.manager;

import com.mmorpg.mbdl.business.container.entity.ContainerEntity;
import com.mmorpg.mbdl.business.role.model.Role;
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

    @PostConstruct
    private void init() {
        self = this;
    }

    public static ContainerManager getInstance() {
        return self;
    }

    public void bindContainerEntity(Role role) {
        ContainerEntity containerEntity = containerEntityIStorage.getOrCreate(role.getRoleId(), id ->
            new ContainerEntity().setRoleId(id)
        );
        role.setContainerEntity(containerEntity);
    }

    public void updateEntity(ContainerEntity containerEntity) {
        containerEntityIStorage.update(containerEntity);
    }
}
