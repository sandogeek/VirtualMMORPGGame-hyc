package com.mmorpg.mbdl.framework.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 抽象的的分发器runnable
 * @author sando
 */
public abstract class AbstractDispatcherRunnable implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDispatcherRunnable.class);
    /**
     * 获取分发id，通常是hashcode
     * @return
     */
    public abstract int getDispatcherId();

    /**
     * 任务名称,统计分类用
     * @return taskName
     */
    public abstract String taskName();

    /**
     * 任务过期时间（默认一毫秒）
     * @return
     */
    protected long timeOver(){
        return TimeUnit.NANOSECONDS.convert(1,TimeUnit.MILLISECONDS);
    }

    /**
     * 自定义的任务
     */
    public abstract void doRun();

    @Override
    public void run() {
        // TODO 统计超时任务
        try{
            doRun();
        }catch (Throwable e){
            logger.error("任务执行失败，抛出异常",e);
        }
    }
}
