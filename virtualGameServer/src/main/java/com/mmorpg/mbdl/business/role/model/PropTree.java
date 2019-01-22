package com.mmorpg.mbdl.business.role.model;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 属性树根节点
 *
 * @author Sando Geek
 * @since v1.0 2019/1/21
 **/
public class PropTree extends PropNode {
    /**
     * 属性树读写锁
     */
    protected ReentrantReadWriteLock treeRWLock = new ReentrantReadWriteLock();

    public PropTree() {
        super(null, 0,"根节点");
        setPropTree(this);
    }

    ReentrantReadWriteLock getTreeRWLock() {
        return treeRWLock;
    }

    @Override
    protected long handleSet(long newValue) {
        if (newValue < 0 ) {
            throw new RuntimeException("不能将根节点的值设置为负值");
        }
        return super.handleSet(newValue);
    }
}
