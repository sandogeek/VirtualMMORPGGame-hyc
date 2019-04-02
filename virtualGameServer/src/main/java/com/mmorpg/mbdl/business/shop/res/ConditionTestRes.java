package com.mmorpg.mbdl.business.shop.res;

import com.mmorpg.mbdl.business.common.condition.Conditions;
import com.mmorpg.mbdl.business.common.condition.ICondition;
import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.framework.resource.annotation.Key;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;
import com.mmorpg.mbdl.framework.resource.exposed.IAfterResLoad;

import java.util.List;

/**
 * 条件测试资源
 *
 * @author Sando Geek
 * @since v1.0 2019/3/29
 **/
@ResDef
public class ConditionTestRes implements IAfterResLoad {
    @Key
    private int key;
    private List<ICondition<Role>> conditionList;
    private transient Conditions<Role> conditions;

    @Override
    public void afterLoad() {
        conditions = new Conditions<>(conditionList);
    }
}
