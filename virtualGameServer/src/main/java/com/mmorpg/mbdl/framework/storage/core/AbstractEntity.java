package com.mmorpg.mbdl.framework.storage.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 抽象实体
 *
 * @author Sando Geek
 * @since v1.0 2019/1/16
 **/
public abstract class AbstractEntity<PK extends Serializable &Comparable<PK>> implements IEntity<PK> {
    @JsonIgnore
    private transient AtomicBoolean canCreateMergeUpdateTask = new AtomicBoolean(true);
    // @JsonIgnore
    // @Transient
    // private AtomicReference<ScheduledFuture> mergeUpdateTaskFutureAtomic = new AtomicReference<>(null);

    public AtomicBoolean getCanCreateMergeUpdateTask() {
        return canCreateMergeUpdateTask;
    }

    // public AtomicReference<ScheduledFuture> getMergeUpdateTaskFutureAtomic() {
    //     return mergeUpdateTaskFutureAtomic;
    // }

}
