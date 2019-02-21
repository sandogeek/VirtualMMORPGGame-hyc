package com.mmorpg.mbdl.business.equip.model.handler;

import com.google.common.base.Preconditions;
import com.mmorpg.mbdl.business.container.model.ItemType;
import com.mmorpg.mbdl.business.container.res.ItemRes;
import com.mmorpg.mbdl.business.equip.manager.EquipHandlerManager;
import com.mmorpg.mbdl.business.equip.model.Equip;
import com.mmorpg.mbdl.business.equip.model.EquipType;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.business.role.model.prop.PropNode;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;

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
    protected WeakHashMap<Role, List<PropNode>> role2PropNodes = new WeakHashMap<>(16);

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
    public Equip equip(Role role, Equip toEquip) {
        Preconditions.checkNotNull(toEquip,"待穿戴装备不能为null");
        Optional.ofNullable(role2PropNodes.get(role)).ifPresent(this::clear);
        itemReses.get(toEquip.getKey()).getPropChangeAfterUse().forEach((propType, aLong) -> {
            PropNode propNode = role.getPropManager().getOrCreateTree(propType)
                    .getOrCreateChild(ItemType.EQUIP.name()).getOrCreateChild(toEquip.getEquipType().name());
            getOrCreatePropNodes(role).add(propNode);
            propNode.set(aLong);
        });
        return doEquip(role,toEquip);
    }

    /**
     * 穿戴此类型的装备特殊部分
     * @param role
     * @param toEquip
     * @return
     */
    public abstract Equip doEquip(Role role, Equip toEquip);
    /**
     * 清空装备带来的所有属性
     * @param propNodes 属性节点
     */
    protected void clear(List<PropNode> propNodes) {
        propNodes.forEach(PropNode::remove);
    }

    protected List<PropNode> getOrCreatePropNodes(Role role) {
        role2PropNodes.computeIfAbsent(role, k -> new ArrayList<>(4));
        return role2PropNodes.get(role);
    }
}
