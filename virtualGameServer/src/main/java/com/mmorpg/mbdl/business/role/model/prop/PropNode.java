package com.mmorpg.mbdl.business.role.model.prop;

import com.google.common.base.MoreObjects;

import java.util.HashMap;
import java.util.Map;

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
    /** 节点名称 */
    private String name;
    private Map<String, PropNode> childPropNodeMap = new HashMap<>();

    public PropNode(PropTree propTree, long initValue,String name) {
        this.propTree = propTree;
        this.value = initValue;
        this.name = name;
    }

    /**
     * 在当前节点下获取或创建属性子节点
     * @param name
     */
    public PropNode getOrCreateChild(String name) {
        PropNode propNode = childPropNodeMap.get(name);
        if (propNode == null) {
            PropNode node = new PropNode(propTree, 0, name);
            addChild(node);
            return node;
        }
        return propNode;
    }

    /**
     * 递归地清除孩子节点造成的值变更
     * @param propNode
     */
    private void clear(PropNode propNode) {
        if (!propNode.childPropNodeMap.isEmpty()) {
            for (PropNode propNodeTemp : propNode.childPropNodeMap.values()) {
                clear(propNodeTemp);
            }
        }
        handleAddAndGet(-propNode.getValue());
    }

    /**
     * 移除当前节点的值
     */
    public void remove() {
        this.parent.removeChild(name);
    }

    /**
     * 删除指定名称的子节点
     * @param name 子节点名称
     */
    public void removeChild(String name) {
        PropNode propNode = getChild(name);
        if (propNode == null) {
            return;
        }
        try {
            propTree.getTreeRWLock().writeLock().lock();
            clear(propNode);
            this.childPropNodeMap.remove(name);
        } finally {
            propTree.getTreeRWLock().writeLock().unlock();
        }
    }
    
    public PropNode getChild(String name) {
        try {
            propTree.getTreeRWLock().readLock().lock();
            return childPropNodeMap.get(name);
        } finally {
            propTree.getTreeRWLock().readLock().unlock();
        }
    }

    /**
     * 在当前节点添加属性子节点
     * @param propNode
     */
    private void addChild(PropNode propNode) {
        try {
            propTree.getTreeRWLock().writeLock().lock();
            childPropNodeMap.put(propNode.name, propNode);
            propNode.setPropTree(this.propTree);
            propNode.setParent(this);
        } finally {
            propTree.getTreeRWLock().writeLock().unlock();
        }
    }

    private long handleSet(long newValue) {
        this.value = newValue;
        return this.value;
    }

    /**
     * 因为{@link PropNode#addAndGet(long)}已经锁住了整棵树，提取出来避免重复加锁
     * @param delta
     * @return
     */
    private long handleAddAndGet(long delta) {
        long tempValue = this.value + delta;
        propTree.addPropValue(delta);
        return handleSet(tempValue);
    }

    /**
     * 增加并获取增加后节点的值
     * @param delta
     */
    public long addAndGet(long delta) {
        try {
            propTree.getTreeRWLock().writeLock().lock();
            return handleAddAndGet(delta);
        } finally {
            propTree.getTreeRWLock().writeLock().unlock();
        }
    }

    /**
     * 把节点设置为指定值
     */
    public PropNode set(long newValue) {
        try {
            propTree.getTreeRWLock().writeLock().lock();
            propTree.addPropValue(newValue - value);
            handleSet(newValue);
            return this;
        } finally {
            propTree.getTreeRWLock().writeLock().unlock();
        }
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


    PropNode setParent(PropNode parent) {
        this.parent = parent;
        return this;
    }

    private PropNode setPropTree(PropTree propTree) {
        this.propTree = propTree;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .toString();
    }
}
