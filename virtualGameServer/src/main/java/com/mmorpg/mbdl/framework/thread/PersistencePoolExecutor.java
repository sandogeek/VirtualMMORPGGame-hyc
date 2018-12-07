package com.mmorpg.mbdl.framework.thread;

import com.mmorpg.mbdl.framework.storage.persistence.PersistenceDelayedTask;
import com.mmorpg.mbdl.framework.thread.task.AbstractTask;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 持久化任务线程池
 * @deprecated 目前不使用定时持久化方案，而是在获取数据时依靠缓存，持久化直接到数据库
 * @author Sando Geek
 * @since v1.0
 **/
@SuppressFBWarnings("UUF_UNUSED_FIELD")
@Deprecated
public class PersistencePoolExecutor {
    private ScheduledThreadPoolExecutor persistenceThreadPool;
    /** 持久化池相关的任务队列
     *  PersistenceDelayedTask 重写了hashcode和equals,每个延迟时长不同的PersistenceDelayedTask对应一个map<String,AbstractTask>。
     *  这个map的键为抽象任务的
     */
    private ConcurrentHashMap<PersistenceDelayedTask, ConcurrentHashMap<String,AbstractTask>> persistenceTaskQueue;
}
