package com.mmorpg.mbdl.framework.storage.persistence;

import com.mmorpg.mbdl.framework.thread.task.DelayedTask;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 持久化延迟任务，出现第一个持久化请求时创建
 *
 * @author Sando Geek
 * @since v1.0
 **/
public class PersistenceDelayedTask extends DelayedTask {
    public PersistenceDelayedTask(Serializable dispatcherId, long delay, TimeUnit timeUnit) {
        super(dispatcherId, delay, timeUnit);
    }

    @Override
    public long getMaxExecute() {
        return super.getMaxExecute();
    }

    @Override
    public Serializable getDispatcherId() {
        return getDelay();
    }

    @Override
    public String taskName() {
        return "持久化延迟任务";
    }

    @Override
    public void execute() {

    }
}
