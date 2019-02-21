package com.mmorpg.mbdl.business.equip.model.handler;

import com.mmorpg.mbdl.business.equip.model.Equip;
import com.mmorpg.mbdl.business.equip.model.EquipType;
import com.mmorpg.mbdl.business.role.model.Role;
import org.springframework.stereotype.Component;

/**
 * 防具处理器
 *
 * @author Sando Geek
 * @since v1.0 2019/2/18
 **/
@Component
public class ArmorHandler extends AbstractEquipHandler {

    @Override
    public EquipType getEquipType() {
        return EquipType.ARMOR;
    }

    @Override
    public Equip doEquip(Role role, Equip toEquip) {
        Equip oldEquip = role.getEquipEntity().getArmor();
        role.getEquipEntity().setArmor(toEquip);
        return oldEquip;
    }
}
