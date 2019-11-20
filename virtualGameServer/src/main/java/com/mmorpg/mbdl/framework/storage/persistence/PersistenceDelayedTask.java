package com.mmorpg.mbdl.framework.storage.persistence;

import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;
import com.mmorpg.mbdl.framework.thread.task.DelayedTask;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 持久化延迟任务，出现第一个持久化请求时创建
 *
 * @author Sando Geek
 * @since v1.0
 **/
public class PersistenceDelayedTask<E extends Dispatchable<T>, T extends Serializable> extends DelayedTask<E, T> {
    public PersistenceDelayedTask(E dispatcher, long delay, TimeUnit timeUnit) {
        super(dispatcher, delay, timeUnit);
    }

    @Override
    public long getMaxExecuteTime() {
        return super.getMaxExecuteTime();
    }

    @Override
    public String taskName() {
        return "持久化延迟任务";
    }

    @Override
    public void execute() {
        // 这里将这个任务对应的map里的任务封装成一个另一个任务异步执行
        // 如保存任务，先将所有保存任务存放为一个集合，然后批量保存
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(getDelay());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PersistenceDelayedTask)) {
            return false;
        }
        PersistenceDelayedTask otherType = (PersistenceDelayedTask) other;
        if (this.getDelay() != otherType.getDelay()){
            return false;
        }
        return true;
    }
}
