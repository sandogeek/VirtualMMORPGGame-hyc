package com.mmorpg.mbdl.framework.task;

import java.util.concurrent.Future;

/**
 * ScheduledThreadPoolExecutor的简单封装
 * @author sando
 */
public interface ITaskExecutor {
    /**
     * 添加同步任务
     * @return
     */
    Future<?> addTask();

    /**
     * 添加延时执行任务
     * @return
     */
    Future<?> addDelayedTask();

    /**
     * 添加固定频率执行的任务
     * 例如周期为3秒，任务执行用一秒，那么这个任务将在2秒后再次执行
     * @return
     */
    Future<?> addFixedRateTask();
}
