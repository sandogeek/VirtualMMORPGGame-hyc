package com.mmorpg.mbdl.framework.thread;

import com.mmorpg.mbdl.framework.storage.persistence.PersistenceDelayedTask;
import com.mmorpg.mbdl.framework.thread.task.AbstractTask;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 持久化任务线程池
 *
 * @author Sando Geek
 * @since v1.0
 **/
public class PersistencePoolExecutor {
    private ScheduledThreadPoolExecutor persistenceThreadPool;
    /** 持久化池相关的任务队列
     *  PersistenceDelayedTask 重写了hashcode和equals,每个延迟时长不同的PersistenceDelayedTask对应一个map。
     */
    private ConcurrentHashMap<PersistenceDelayedTask, AtomicReference<ConcurrentHashMap<String,AbstractTask>>> persistenceTaskQueue;
}
