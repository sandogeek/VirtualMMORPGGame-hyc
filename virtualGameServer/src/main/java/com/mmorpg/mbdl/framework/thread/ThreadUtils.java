package com.mmorpg.mbdl.framework.thread;

import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;
import com.mmorpg.mbdl.framework.thread.task.AbstractTask;

import java.io.Serializable;

/**
 * 线程工具类
 *
 * @author Sando Geek
 * @since v1.0 2019/11/11
 **/
public class ThreadUtils {
    /**
     * 当前线程正在执行哪个Task
     */
    private static ThreadLocal<AbstractTask<? extends Dispatchable<? extends Serializable>>> taskThreadLocal = new ThreadLocal<>();

    /**
     * 获取当前线程正在执行的任务的可分发对象
     * @throws RuntimeException 此方法必须在任务中{@link AbstractTask}包裹的代码中调用
     * @return
     */
    public static Dispatchable<? extends Serializable> currentThreadDispatchable() {
        return currentThreadTask().getDispatcher();
    }

    /**
     * 获取当前线程正在执行的任务
     * @throws RuntimeException 此方法必须在任务中{@link AbstractTask}包裹的代码中调用
     * @return
     */
    public static AbstractTask<? extends Dispatchable<? extends Serializable>> currentThreadTask() {
        AbstractTask<? extends Dispatchable<? extends Serializable>> abstractTask = taskThreadLocal.get();
        if (abstractTask == null) {
            throw new RuntimeException("此方法必须在任务中AbstractTask任务内运行的代码中调用");
        }
        return abstractTask;
    }


    /**
     * 设置当前线程正在执行的抽象任务
     * @param abstractTask
     */
    public static void setCurrentThreadTask(AbstractTask<? extends Dispatchable<? extends Serializable>> abstractTask) {
        taskThreadLocal.set(abstractTask);
    }

    /**
     * 移除当前线程对应的{@link AbstractTask},任务结束时调用
     */
    public static void removeCurrentThreadTask() {
        taskThreadLocal.remove();
    }

    private ThreadUtils() {
    }
}
