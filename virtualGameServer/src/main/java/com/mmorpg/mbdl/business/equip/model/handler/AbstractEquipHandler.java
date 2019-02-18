package com.mmorpg.mbdl.business.equip.model.handler;

import com.mmorpg.mbdl.business.container.res.ItemRes;
import com.mmorpg.mbdl.business.equip.manager.EquipHandlerManager;
import com.mmorpg.mbdl.business.equip.model.Equip;
import com.mmorpg.mbdl.business.equip.model.EquipType;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * 抽象装备处理器
 *
 * @author Sando Geek
 * @since v1.0 2019/2/18
 **/
public abstract class AbstractEquipHandler {
    @Autowired
    private EquipHandlerManager equipHandlerManager;
    @Autowired
    protected IStaticRes<Integer, ItemRes> itemReses;

    @PostConstruct
    public void register() {
        equipHandlerManager.register(this);
    }

    /**
     * 获取装备类型
     *
     * @return
     */
    public abstract EquipType getEquipType();

    /**
     * 穿戴此类型的装备
     *
     * @param role    穿戴装备的角色
     * @param toEquip 待穿戴的装备
     * @return 被替换下来的装备，如果原来没有装备则返回null
     */
    public abstract Equip equip(Role role, Equip toEquip);
}
