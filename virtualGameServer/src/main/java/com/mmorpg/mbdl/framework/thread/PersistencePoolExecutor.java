package com.mmorpg.mbdl.framework.thread;

import com.mmorpg.mbdl.framework.thread.task.AbstractTask;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 持久化任务线程池
 *
 * @author Sando Geek
 * @since v1.0
 **/
public class PersistencePoolExecutor {
    private ScheduledThreadPoolExecutor persistenceThreadPool;
    // <延迟任务延迟时间,<AbstractTask,>>
    private ConcurrentHashMap<Long, ConcurrentHashMap<String,AbstractTask>> persistenceTaskQueue;
}
