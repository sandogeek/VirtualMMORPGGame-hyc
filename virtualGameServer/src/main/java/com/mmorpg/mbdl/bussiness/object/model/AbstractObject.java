package com.mmorpg.mbdl.bussiness.object.model;

import java.util.Objects;

/**
 * 唯一对象
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public abstract class AbstractObject {
    /**
     * 唯一id
     */
    private Long objectId;
    /**
     * 对象名称
     */
    private String name;

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    /**
     * 设置对象名称
     * @param name 对象名称
     */
    public abstract void setName(String name);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractObject that = (AbstractObject) o;
        return Objects.equals(objectId, that.objectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId);
    }
}
