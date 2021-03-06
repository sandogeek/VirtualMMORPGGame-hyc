package com.mmorpg.mbdl.business.equip.manager;

import com.mmorpg.mbdl.business.common.IRoleEntityManager;
import com.mmorpg.mbdl.business.equip.entity.EquipEntity;
import com.mmorpg.mbdl.business.equip.model.Equip;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(EquipManager.class);
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
        equipEntity.getEquipTypeEquipMap().values().forEach(equip -> equip(role,equip));
    }

    @Override
    public void updateEntity(EquipEntity entity) {
        equipEntityIStorage.update(entity);
    }

    @Override
    public void mergeUpdateEntity(EquipEntity entity) {
        equipEntityIStorage.mergeUpdate(entity);
    }

    /**
     * 穿戴装备
     *
     * @param role    穿戴装备的人
     * @param toEquip 待穿戴的装备
     * @return 被替换下来的装备，如果原来没有装备则返回null
     */
    public Equip equip(Role role, Equip toEquip) {
        return EquipHandlerManager.getInstance().getEquipHandlerByEquipType(toEquip.getEquipType()).equip(role, toEquip);
    }
}
