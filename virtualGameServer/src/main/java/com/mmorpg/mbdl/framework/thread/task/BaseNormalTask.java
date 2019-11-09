package com.mmorpg.mbdl.framework.thread.task;

import java.io.Serializable;

/**
 * 普通任务
 * @author Sando
 */
public abstract class BaseNormalTask<K extends Serializable> extends AbstractTask<K> {
    public BaseNormalTask(K dispatcherId) {
        super(dispatcherId);
    }

    @Override
    public TaskType taskType() {
        return TaskType.TASK;
    }

}
