package com.mmorpg.mbdl.framework.thread.task;

/**
 * 对任务队列而言同步执行的任务
 */
public abstract class Task extends AbstractTask {

    @Override
    public TaskType taskType() {
        return TaskType.TASK;
    }

}
