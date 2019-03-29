package com.mmorpg.mbdl.common.condition.impl.role;

import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.common.condition.ICondition;

/**
 * 验证主体为角色的条件
 *
 * @author Sando Geek
 * @since v1.0 2019/3/29
 **/
public abstract class BaseRoleCondition implements ICondition<Role> {
    /**
     * @param obj 验证的主体对象
     * @return
     */
    @Override
    public abstract boolean verify(Role obj);
}
