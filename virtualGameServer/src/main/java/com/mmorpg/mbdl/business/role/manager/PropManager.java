package com.mmorpg.mbdl.business.role.manager;

import com.mmorpg.mbdl.business.role.model.Role;
import com.mmorpg.mbdl.business.role.model.PropTree;
import com.mmorpg.mbdl.business.role.model.PropType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 属性管理器
 * @author Sando Geek
 * @since v1.0 2019/1/21
 **/
public class PropManager {
    private Role owner;
    private Map<PropType, PropTree> type2Tree = new ConcurrentHashMap<>();

    public PropManager(Role owner) {
        this.owner = owner;
        for (PropType propType :
                PropType.values()) {
            type2Tree.put(propType,new PropTree());
        }
    }

}
