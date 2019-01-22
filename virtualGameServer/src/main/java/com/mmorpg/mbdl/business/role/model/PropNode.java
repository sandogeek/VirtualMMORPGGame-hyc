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
     * 删除指定名称的子节点
     * @param name 子节点名称
     */
    public void deleteChild(String name) {
        PropNode propNode = getChild(name);
        if (propNode == null) {
            return;
        }
        try {
            propTree.getTreeRWLock().writeLock().lock();
            childPropNodeMap.remove(name);
            handleAddAndGet(-propNode.getValue());
            if (childPropNodeMap.isEmpty()) {
                isLeafNode = true;
            }
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
            this.isLeafNode = false;
            childPropNodeMap.put(propNode.name, propNode);
            propNode.setPropTree(this.propTree);
            propNode.setParent(this);
        } finally {
            propTree.getTreeRWLock().writeLock().unlock();
        }
    }

    protected long handleSet(long newValue) {
        this.value = newValue;
        return this.value;
    }

    /**
     * 因为{@link PropNode#addAndGet(long)}已经锁住了整棵树，提取出来避免重复加锁
     * @param delta
     * @return
     */
    protected long handleAddAndGet(long delta) {
        long tempValue = this.value + delta;
        if (parent != null) {
            parent.handleAddAndGet(delta);
        }
        return handleSet(tempValue);
    }

    /**
     * 增加并获取增加后节点的值
     * @param delta
     */
    public long addAndGet(long delta) {
        check();
        try {
            propTree.getTreeRWLock().writeLock().lock();
            return handleAddAndGet(delta);
        } finally {
            propTree.getTreeRWLock().writeLock().unlock();
        }
        // check(delta);
        // try {
        //     propTree.getTreeRWLock().writeLock().lock();
        //     if (parent != null) {
        //         parent.handleAddAndGet(delta);
        //     }
        //     return handleAddAndGet(delta);
        // } finally {
        //     propTree.getTreeRWLock().writeLock().unlock();
        // }
    }

    protected void check() {
        try {
            propTree.getTreeRWLock().readLock().lock();
            if (!isLeafNode) {
                throw new RuntimeException(String.format("此函数不能用于更改非叶子节点的值,当前节点[%s]", this.name));
            }
        } finally {
            propTree.getTreeRWLock().readLock().unlock();
        }


    }

    /**
     * 把节点设置为指定值
     */
    public void set(long newValue) {
        check();
        try {
            propTree.getTreeRWLock().writeLock().lock();
            if (parent != null) {
                parent.handleAddAndGet(newValue - value);
            }
            handleSet(newValue);
        } finally {
            propTree.getTreeRWLock().writeLock().unlock();
        }
        // long delta = newValue - value;
        // if (delta == 0) {
        //     return;
        // }
        // addAndGet(delta);
    }

    /**
     * 获取此节点的值
     * @return
     */
    public long getValue() {
        try {
            propTree.getTreeRWLock().readLock().lock();
            return doGetValue();
        } finally {
            propTree.getTreeRWLock().readLock().unlock();
        }
    }

    protected long doGetValue() {
        return value;
    }

    PropNode setParent(PropNode parent) {
        this.parent = parent;
        return this;
    }

    PropNode setPropTree(PropTree propTree) {
        this.propTree = propTree;
        return this;
    }
}
