package com.mmorpg.mbdl.business.common.condition.impl.role;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.business.role.model.prop.PropType;
import com.mmorpg.mbdl.business.common.condition.anno.ConditionDesc;

/**
 * 角色等级条件
 *
 * @author Sando Geek
 * @since v1.0 2019/3/29
 **/
@ConditionDesc("只配置minLevel则只检查最低等级，只配置maxLevel则只检查最高等级，否则同时检查最高最低等级")
@JsonTypeName("roleLevel")
public class RoleLevelCondition extends BaseRoleCondition {
    enum CheckType {
        /**
         * 只检查最低等级
         */
        checkMin,
        /**
         * 只检查最高等级
         */
        checkMax,
        /**
         * 同时检查最高最低等级
         */
        checkAll
    }
    private Integer minLevel;
    private Integer maxLevel;
    private transient CheckType checkType;

    @Override
    public boolean verify(Role role) {
        switch (checkType) {
            case checkMin: {
                if (role.getPropManager().getPropValueOf(PropType.LEVEL) >= minLevel) {
                    return true;
                }
            }
            case checkMax: {
                if (role.getPropManager().getPropValueOf(PropType.LEVEL) <= maxLevel) {
                    return true;
                }
            }
            case checkAll: {
                if (role.getPropManager().getPropValueOf(PropType.LEVEL) >= minLevel &&
                        role.getPropManager().getPropValueOf(PropType.LEVEL) <= maxLevel) {
                    return true;
                }
            }
            default:
        }
        return false;
    }

    @Override
    public void check() {
        if (minLevel == null && maxLevel == null) {
            throw new RuntimeException("等级条件至少要配置最低等级min或最高等级max中的一个");
        }
        if (maxLevel == null) {
            checkType = CheckType.checkMin;
        } else if (minLevel == null) {
            checkType = CheckType.checkMax;
        } else {
            checkType = CheckType.checkAll;
        }
    }

    public void setMinLevel(Integer minLevel) {
        this.minLevel = minLevel;
    }

    public void setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }
}
