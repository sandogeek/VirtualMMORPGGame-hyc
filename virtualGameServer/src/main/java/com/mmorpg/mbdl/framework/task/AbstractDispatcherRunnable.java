package com.mmorpg.mbdl.framework.task;

/**
 * 抽象的的分发器runnable
 * @author sando
 */
public abstract class AbstractDispatcherRunnable implements Runnable {
    /**
     * 获取分发id，通常是hashcode
     * @return
     */
    public abstract int getDispatcherId();

    @Override
    public void run() {

    }
}
