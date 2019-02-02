package com.mmorpg.mbdl.business.equip.entity;

import com.mmorpg.mbdl.business.equip.model.Equip;
import com.mmorpg.mbdl.framework.storage.annotation.JetCacheConfig;
import com.mmorpg.mbdl.framework.storage.core.AbstractEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;

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
    @Type(type = "json")
    private Equip weapon;
    /**
     * 防具
     */
    @Type(type = "json")
    private Equip armor;

    public EquipEntity() {
    }

    public EquipEntity(long roleId) {
        this.roleId = roleId;
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

    @Override
    public Long getId() {
        return this.roleId;
    }
}