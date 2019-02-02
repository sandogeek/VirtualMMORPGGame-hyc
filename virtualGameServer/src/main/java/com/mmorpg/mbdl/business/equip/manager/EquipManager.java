package com.mmorpg.mbdl.business.equip.manager;

import com.mmorpg.mbdl.business.common.IRoleEntityManager;
import com.mmorpg.mbdl.business.equip.entity.EquipEntity;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 装备管理器
 *
 * @author Sando Geek
 * @since v1.0 2019/2/1
 **/
@Component
public class EquipManager implements IRoleEntityManager<EquipEntity> {
    private static EquipManager self;

    @Autowired
    private IStorage<Long, EquipEntity> equipEntityIStorage;

    @PostConstruct
    private void init() {
        self = this;
    }

    public static EquipManager getInstance() {
        return self;
    }

    @Override
    public void bindEntity(Role role) {
        EquipEntity equipEntity = equipEntityIStorage.getOrCreate(role.getRoleId(), EquipEntity::new);
        role.setEquipEntity(equipEntity);
    }

    @Override
    public void updateEntity(EquipEntity entity) {
        equipEntityIStorage.update(entity);
    }

    @Override
    public void mergeUpdateEntity(EquipEntity entity) {
        equipEntityIStorage.mergeUpdate(entity);
    }
}
