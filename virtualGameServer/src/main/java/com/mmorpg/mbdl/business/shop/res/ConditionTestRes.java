package com.mmorpg.mbdl.business.shop.res;

import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.common.condition.BaseResWithConditions;
import com.mmorpg.mbdl.common.condition.Conditions;
import com.mmorpg.mbdl.framework.resource.annotation.Key;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

/**
 * 条件测试资源
 *
 * @author Sando Geek
 * @since v1.0 2019/3/29
 **/
@ResDef
public class ConditionTestRes extends BaseResWithConditions {
    @Key
    private int key;
    private Conditions<Role> conditions;
}
