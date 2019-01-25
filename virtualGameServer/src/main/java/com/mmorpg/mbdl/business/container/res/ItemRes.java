package com.mmorpg.mbdl.business.container.res;

import com.mmorpg.mbdl.business.role.model.prop.PropType;
import com.mmorpg.mbdl.framework.resource.annotation.Key;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;

import java.util.Map;

/**
 * 物品配置表
 *
 * @author Sando Geek
 * @since v1.0 2019/1/25
 **/
@ResDef
public class ItemRes {
    @Key
    private int key;
    private String name;
    private Map<PropType,Long> propChangeAfterUse;
    private int maxAmount;

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public Map<PropType, Long> getPropChangeAfterUse() {
        return propChangeAfterUse;
    }
}
