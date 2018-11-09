package com.mmorpg.mbdl.framework.storage.core;

import java.io.Serializable;

/**
 * 实体标识
 * @param <PK> Primary Key 实体类主键
 */
public interface IEntity<PK extends Serializable&Comparable<PK>> {
    /**
     * 获取@Entity对象的主键
     * @return
     */
    PK getId();
}
