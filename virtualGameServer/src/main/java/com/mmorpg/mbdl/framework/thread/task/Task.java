package com.mmorpg.mbdl.framework.thread.task;

import java.io.Serializable;

/**
 * 普通任务
 */
public abstract class Task extends AbstractTask {
    public Task(Serializable dispatcherId) {
        super(dispatcherId);
    }

    @Override
    public TaskType taskType() {
        return TaskType.TASK;
    }

}
