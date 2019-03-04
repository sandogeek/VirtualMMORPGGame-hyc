package com.mmorpg.mbdl.business.role.model.prop;

import com.mmorpg.mbdl.business.object.model.AbstractCreature;

/**
 * 属性类型，每个类型对应一颗属性树
 *
 * @author Sando Geek
 * @since v1.0 2019/1/21
 **/
public enum PropType {
    /**
     * 当前血量
     */
    CURRENT_HP,
    /**
     * 当前蓝量
     */
    CURRENT_MP,
    /**
     * 最大血量
     */
    MAX_HP,
    /**
     * 最大蓝量
     */
    MAX_MP,
    /**
     * 攻击力
     */
    ATTACK,
    /**
     * 防御力
     */
    DEFENCE,
    /**
     * 等级
     */
    LEVEL,
    /**
     * 所在场景ID
     */
    SCENE_ID,
    /**
     * 经验
     */
    EXP
    ;
    public PropTree create(AbstractCreature abstractCreature) {
        return new PropTree();
    }

}
