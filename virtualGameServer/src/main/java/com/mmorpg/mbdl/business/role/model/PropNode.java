package com.mmorpg.mbdl.business.role.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 属性节点
 *
 * @author Sando Geek
 * @since v1.0 2019/1/21
 **/
public class PropNode {
    private PropTree propTree;
    private PropNode parent;
    protected long value;
    /** 属性来源名称 */
    private String name;
    private Map<String, PropNode> childPropNodeMap = new ConcurrentHashMap<>();
    /** 是否是叶子节点 */
    private boolean isLeafNode = true;

    public PropNode(PropTree propTree, long initValue) {
        this.propTree = propTree;
        this.value = initValue;
    }

    /**
     * 在当前节点下获取或创建属性子节点
     * @param name
     */
    public PropNode getOrCreateChild(String name) {
        PropNode propNode = childPropNodeMap.get(name);
        if (propNode == null) {
            PropNode node = new PropNode(propTree, 0);
            addChild(node);
            return node;
        }
        return propNode;
    }

    /**
     * 删除指定名称的子节点
     * @param name 子节点名称
     */
    public void deleteChild(String name) {
        PropNode propNode = getChild(name);
        if (propNode == null) {
            return;
        }
        propNode.addAndGet(-propNode.getValue());
        childPropNodeMap.remove(name);
    }
    
    public PropNode getChild(String name) {
        return childPropNodeMap.get(name);
    }

    /**
     * 在当前节点添加属性子节点
     * @param propNode
     */
    private void addChild(PropNode propNode) {
        this.isLeafNode = false;
        childPropNodeMap.put(propNode.name, propNode);
    }

    protected long handleAddAndGet(long delta) {
        this.value += delta;
        return this.value;
    }

    /**
     * 增加并获取增加后节点的值
     * @param delta
     */
    public long addAndGet(long delta) {
        check(delta);
        try {
            propTree.getTreeRWLock().writeLock().lock();
            parent.handleAddAndGet(delta);
            return handleAddAndGet(delta);
        } finally {
            propTree.getTreeRWLock().writeLock().unlock();
        }
    }

    protected void check(long delta) {
        if (!isLeafNode) {
            throw new RuntimeException(String.format("此函数不能用于更改非叶子节点的值,当前节点[%s]", this.name));
        }
        if ((propTree.value + delta) < 0 ) {
            throw new RuntimeException("此变更将导致根节点的值小于0");
        }
    }

    /**
     * 把节点设置为指定值
     */
    public void set(long newValue) {
        long delta = newValue - value;
        if (delta == 0) {
            return;
        }
        addAndGet(delta);
    }

    /**
     * 获取此节点的值
     * @return
     */
    public long getValue() {
        try {
            propTree.getTreeRWLock().readLock().lock();
            return value;
        } finally {
            propTree.getTreeRWLock().readLock().unlock();
        }
    }

    public PropNode setParent(PropNode parent) {
        this.parent = parent;
        return this;
    }

    PropNode setPropTree(PropTree propTree) {
        this.propTree = propTree;
        return this;
    }
}
