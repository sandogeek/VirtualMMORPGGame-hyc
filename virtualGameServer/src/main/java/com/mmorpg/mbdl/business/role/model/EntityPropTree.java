package com.mmorpg.mbdl.business.role.model;

/**
 * 实体属性树，用于管理存在于数据库字段中的属性
 *
 * @author Sando Geek
 * @since v1.0 2019/1/22
 **/
public abstract class EntityPropTree extends PropTree {
    @Override
    protected final long handleSet(long newValue) {
        if (newValue < 0 ) {
            throw new RuntimeException("不能将根节点的值设置为负值");
        }
        return doHandleSet(newValue);
    }

    protected abstract long doHandleSet(long newValue);

    @Override
    protected abstract long doGetValue();
}
