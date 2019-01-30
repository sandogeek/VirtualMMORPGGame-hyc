package com.mmorpg.mbdl.business.equip.entity;

import com.mmorpg.mbdl.business.container.model.AbstractItem;
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
    private Map<EquipType, AbstractItem> type2ItemMap = new HashMap<>(EquipType.values().length);

    public Map<EquipType, AbstractItem> getType2ItemMap() {
        return type2ItemMap;
    }

    @Override
    public Long getId() {
        return this.roleId;
    }
}