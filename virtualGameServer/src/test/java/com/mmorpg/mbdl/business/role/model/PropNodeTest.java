package com.mmorpg.mbdl.business.role.model;

import com.mmorpg.mbdl.business.container.model.ItemType;
import com.mmorpg.mbdl.business.equip.model.EquipType;
import com.mmorpg.mbdl.business.role.model.prop.PropNode;
import com.mmorpg.mbdl.business.role.model.prop.PropTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PropNodeTest {
    private PropTree propTree = new PropTree();

    @BeforeEach
    void setUp() {
        propTree.setRootNodeValue(100L);
        PropNode testNode1 = propTree.getOrCreateChild("testNode1").set(100L);
        PropNode testNode2 = propTree.getOrCreateChild("testNode2").set(200L);
        testNode1.getOrCreateChild("node1_1").set(50L);
        testNode1.getOrCreateChild("node1_2").set(50L);
        propTree.getOrCreateChild(ItemType.EQUIP.name()).getOrCreateChild(EquipType.WEAPON.name()).set(100L);
    }

    @Test
    void getOrCreateChild() {
        Assertions.assertEquals(50L,propTree.getChild("testNode1").getChild("node1_1").getValue());
    }

    @Test
    void deleteChild() {
        propTree.removeChild("testNode1");
        Assertions.assertEquals(300L,propTree.getPropValue());
        propTree.removeChild("testNode2");
        Assertions.assertEquals(100L,propTree.getPropValue());
    }

    @Test
    void addAndGet() {
        PropNode propNode = propTree.getChild("testNode1").getChild("node1_1");
        propNode.addAndGet(100L);
        Assertions.assertEquals(150L,propNode.getValue());
        Assertions.assertEquals(600L,propTree.getPropValue());
    }

    @Test
    void set() {
        PropNode propNode = propTree.getChild("testNode1").getChild("node1_2");
        propNode.set(200L);
        Assertions.assertEquals(200L,propNode.getValue());
    }
}