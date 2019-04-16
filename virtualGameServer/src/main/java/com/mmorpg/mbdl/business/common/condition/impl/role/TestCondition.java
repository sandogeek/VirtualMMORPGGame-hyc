package com.mmorpg.mbdl.business.common.condition.impl.role;

import com.mmorpg.mbdl.business.role.model.Role;

/**
 * 测试条件
 *
 * @author Sando Geek
 * @since v1.0 2019/4/16
 **/
// @JsonTypeName("roleLevel")
public class TestCondition extends BaseRoleCondition {
    @Override
    public boolean verify(Role obj) {
        return false;
    }

    @Override
    public void check() {

    }
}
