package com.mmorpg.mbdl.business.equip.entity;

import com.mmorpg.mbdl.business.equip.model.Equip;
import com.mmorpg.mbdl.business.equip.model.EquipType;
import com.mmorpg.mbdl.framework.storage.annotation.JetCacheConfig;
import com.mmorpg.mbdl.framework.storage.core.AbstractEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

/**
 * 装备实体
 *
 * @author Sando Geek
 * @since v1.0 2019/1/30
 **/
@Entity
@JetCacheConfig
public class EquipEntity extends AbstractEntity<Long> {
    @Id
    private long roleId;
    @Type(type = "json")
    private Map<EquipType, Equip> type2EquipMap = new HashMap<>(EquipType.values().length);

    public Map<EquipType, Equip> getType2EquipMap() {
        return type2EquipMap;
    }

    @Override
    public Long getId() {
        return this.roleId;
    }
}