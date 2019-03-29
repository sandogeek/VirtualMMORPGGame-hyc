package com.mmorpg.mbdl.business.equip.entity;

import com.mmorpg.mbdl.business.common.orm.JsonType;
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
    /**
     * 武器
     */
    @Type(type = JsonType.NAME)
    private Equip weapon;
    /**
     * 防具
     */
    @Type(type = JsonType.NAME)
    private Equip armor;

    public EquipEntity() {
    }

    public EquipEntity(long roleId) {
        this.roleId = roleId;
    }

    public Map<EquipType, Equip> getEquipTypeEquipMap() {
        Map<EquipType, Equip> equipTypeEquipMap = new HashMap<>(2);
        addEquip(equipTypeEquipMap, getWeapon());
        addEquip(equipTypeEquipMap, getArmor());
        return equipTypeEquipMap;
    }

    public Equip getWeapon() {
        return weapon;
    }

    public EquipEntity setWeapon(Equip weapon) {
        this.weapon = weapon;
        return this;
    }

    public Equip getArmor() {
        return armor;
    }

    public EquipEntity setArmor(Equip armor) {
        this.armor = armor;
        return this;
    }

    private void addEquip(Map<EquipType, Equip> equipTypeEquipMap,Equip equip) {
        if (equip == null) {
            return;
        }
        equipTypeEquipMap.put(equip.getEquipType(),equip);
    }

    @Override
    public Long getId() {
        return this.roleId;
    }
}