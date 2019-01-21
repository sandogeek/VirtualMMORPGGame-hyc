package com.mmorpg.mbdl.business.role.model;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 属性树
 *
 * @author Sando Geek
 * @since v1.0 2019/1/21
 **/
public class PropTree extends PropNode {
    /**
     * 属性树读写锁
     */
    private ReentrantReadWriteLock treeRWLock = new ReentrantReadWriteLock();

    public PropTree() {
        super(null, 0);
        setPropTree(this);
    }

    ReentrantReadWriteLock getTreeRWLock() {
        return treeRWLock;
    }

}
