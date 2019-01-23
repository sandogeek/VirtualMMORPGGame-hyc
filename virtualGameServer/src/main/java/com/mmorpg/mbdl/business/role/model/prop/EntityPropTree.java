package com.mmorpg.mbdl.business.role.model.prop;

/**
 * 实体属性树，用于管理存在于数据库字段中的属性
 *
 * @author Sando Geek
 * @since v1.0 2019/1/22
 **/
public abstract class EntityPropTree extends PropTree {
    @Override
    protected abstract void doSetPropValue(long newValue);

    @Override
    protected abstract long doGetPropValue();
}
