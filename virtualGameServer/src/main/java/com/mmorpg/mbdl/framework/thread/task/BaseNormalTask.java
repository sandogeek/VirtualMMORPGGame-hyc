package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;

import java.io.Serializable;

/**
 * 普通任务
 * @author Sando
 */
public abstract class BaseNormalTask<K extends Dispatchable<? extends Serializable>> extends AbstractTask<K> {
    public BaseNormalTask(K dispatcher) {
        super(dispatcher);
    }

    @Override
    public TaskType taskType() {
        return TaskType.TASK;
    }

}
