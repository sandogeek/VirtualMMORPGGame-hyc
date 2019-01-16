package com.mmorpg.mbdl.framework.storage.core;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 抽象实体
 *
 * @author Sando Geek
 * @since v1.0 2019/1/16
 **/
public abstract class AbstractEntity<PK extends Serializable &Comparable<PK>> implements IEntity<PK> {
    @Transient
    private AtomicBoolean canCreateUpdateDelayTask = new AtomicBoolean(true);

    public AtomicBoolean getCanCreateUpdateDelayTask() {
        return canCreateUpdateDelayTask;
    }
}
