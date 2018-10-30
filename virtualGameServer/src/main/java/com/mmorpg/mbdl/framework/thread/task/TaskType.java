package com.mmorpg.mbdl.framework.thread.task;

/**
 * 任务类型<br>
 * 注意不同的任务类型需要不同的参数
 * @author sando
 */
public enum TaskType {
    /**
     * 同步任务
     */
    TASK,
    /**
     * 延时执行的任务
     */
    DELAYED_TASK,
    FIXED_RATE_TASK;
}
