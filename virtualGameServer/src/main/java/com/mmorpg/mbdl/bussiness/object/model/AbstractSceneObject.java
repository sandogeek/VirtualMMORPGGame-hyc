package com.mmorpg.mbdl.bussiness.object.model;

import java.util.Objects;

/**
 * 场景对象
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public abstract class AbstractSceneObject {
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

    public abstract SceneObjectType getObjectType();

    /**
     * 设置对象名称
     * @param name 对象名称
     */
    public AbstractSceneObject setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractSceneObject that = (AbstractSceneObject) o;
        return Objects.equals(objectId, that.objectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId);
    }
}
