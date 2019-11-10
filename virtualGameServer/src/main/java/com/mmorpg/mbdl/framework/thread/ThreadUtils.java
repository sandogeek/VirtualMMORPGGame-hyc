package com.mmorpg.mbdl.framework.thread;

import com.mmorpg.mbdl.framework.thread.task.AbstractTask;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    private static Map<Thread, AbstractTask<? extends Serializable>> threadTaskQueueMap = new ConcurrentHashMap<>();

    /**
     * 获取当前线程正在执行的任务所在的队列的唯一标识
     * @throws RuntimeException 此方法必须在任务中{@link AbstractTask}包裹的代码中调用
     * @return
     */
    public static Serializable currentThreadTaskQueueId() {
        return currentThreadTask().getTaskQueue().getKey();
    }

    /**
     * 获取当前线程正在执行的任务
     * @throws RuntimeException 此方法必须在任务中{@link AbstractTask}包裹的代码中调用
     * @return
     */
    public static AbstractTask<? extends Serializable> currentThreadTask() {
        AbstractTask<? extends Serializable> abstractTask = threadTaskQueueMap.get(Thread.currentThread());
        if (abstractTask == null) {
            throw new RuntimeException("此方法必须在任务中AbstractTask任务内运行的代码中调用");
        }
        return abstractTask;
    }


    /**
     * 设置当前线程正在执行的抽象任务
     * @param abstractTask
     */
    public static void setCurrentThreadTask(AbstractTask<? extends Serializable> abstractTask) {
        threadTaskQueueMap.put(Thread.currentThread(), abstractTask);
    }

    private ThreadUtils() {
    }
}
