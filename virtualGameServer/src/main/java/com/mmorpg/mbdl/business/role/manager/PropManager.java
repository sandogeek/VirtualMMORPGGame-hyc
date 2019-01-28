package com.mmorpg.mbdl.business.role.manager;

import com.mmorpg.mbdl.business.object.model.AbstractCreature;
import com.mmorpg.mbdl.business.role.model.prop.PropTree;
import com.mmorpg.mbdl.business.role.model.prop.PropType;

import java.util.HashMap;
import java.util.Map;

/**
 * 属性管理器
 * @author Sando Geek
 * @since v1.0 2019/1/21
 **/
public class PropManager {
    private AbstractCreature owner;
    private Map<PropType, PropTree> type2Tree = new HashMap<>(12);
    /** 节点名称到其对应的所有节点的集合，方便快速找到所有节点 */
    // private Multimap<String, PropNode> name2Node = ArrayListMultimap.create();

    public PropManager(AbstractCreature owner) {
        this.owner = owner;
    }

    public PropTree getOrCreateTree(PropType propType) {
        PropTree propTree = type2Tree.get(propType);
        if (propTree == null) {
            return type2Tree.put(propType, propType.create(owner));
        }
        return propTree;
    }

    public PropTree getPropTreeByType(PropType propType) {
        return type2Tree.get(propType);
    }

    /**
     * 根据属性类型获取属性值
     * @param propType
     * @return
     */
    public long getPropValueOf(PropType propType) {
        return type2Tree.get(propType).getPropValue();
    }

    public void setRootNodeValueOnType(PropType propType, long newValue) {
        type2Tree.get(propType).setRootNodeValue(newValue);
    }
}
