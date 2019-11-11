package com.mmorpg.mbdl.business.object.model;

import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;

import java.util.Objects;

/**
 * 场景对象
 *
 * @author Sando Geek
 * @since v1.0 2018/12/11
 **/
public abstract class AbstractSceneObject implements Dispatchable<Long> {
    /**
     * 唯一id
     */
    private Long objectId;
    /**
     * 对象名称
     */
    private String name;

    public AbstractSceneObject(Long objectId, String name) {
        this.objectId = objectId;
        this.name = name;
    }

    public Long getObjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public abstract SceneObjectType getObjectType();

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

    @Override
    public Long dispatchId() {
        return objectId;
    }
}
