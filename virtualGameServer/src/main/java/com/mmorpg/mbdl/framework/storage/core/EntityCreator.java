package com.mmorpg.mbdl.framework.storage.core;

@FunctionalInterface
public interface EntityCreator<PK, E> {
    /**
     * 根据id创建一个实体
     * @param id 主键
     * @return 实体
     */
    E create(PK id);
}
