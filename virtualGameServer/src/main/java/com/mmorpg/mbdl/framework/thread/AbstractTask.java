package com.mmorpg.mbdl.framework.thread;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 抽象的的分发器runnable
 * @author sando
 */
public abstract class AbstractTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(AbstractTask.class);
    private StopWatch stopWatch = new StopWatch();
    {
        // 创建时开始计时
        stopWatch.start();
    }

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
     * 最大延迟时间,默认1毫秒
     */
    protected long maxDelay(){
        return TimeUnit.NANOSECONDS.convert(1,TimeUnit.MILLISECONDS);
    }
    /**
     * 执行任务最大耗时(时间单位TimeUnit.NANOSECONDS)(默认一毫秒),可在具体任务中覆盖修改
     * @return
     */
    protected long maxExecute(){
        return TimeUnit.NANOSECONDS.convert(1,TimeUnit.MILLISECONDS);
    }

    /**
     * 使用的logger,可以为不同的任务定制不同的logger
     * @return
     */
    protected Logger logger(){
        return AbstractTask.logger;
    }

    /**
     * 是否打印日志
     */
    protected boolean logOrNot(){
        return true;
    }

    /**
     * 任务延迟过长打印
     */
    protected void logIfOverDelay(long delayTime,long executeTime,Logger targetLogger){
        targetLogger.warn("任务:{}执行延时超出预期，delay={}ms,execute={}ms",this.taskName(),delayTime/1000000F,executeTime/1000000F);
    }
    /**
     * 任务执行时间过长打印
     */
    protected void logIfOverExecute(long delayTime,long executeTime,Logger targetLogger){
        targetLogger.warn("任务:{}执行时间超出预期，delay={}ms,execute={}ms",this.taskName(),delayTime/1000000F,executeTime/1000000F);
    }
    protected void logIfNormal(long delayTime,long executeTime,Logger targetLogger){
        targetLogger.info("任务:{},delay={}ms,execute={}ms",this.taskName(),delayTime/1000000F,executeTime/1000000F);
    }
    /**
     * 打印日志
     */
    private void log(long delayTime,long executeTime){
        long maxDelayTime = this.maxDelay();
        long maxExecuteTime = this.maxExecute();
        Logger targetLogger = this.logger();
        if (delayTime > maxDelayTime){
            this.logIfOverDelay(delayTime,executeTime,targetLogger);
        }else if (executeTime > maxExecuteTime){
            this.logIfOverExecute(delayTime,executeTime,targetLogger);
        }else {
            this.logIfNormal(delayTime,executeTime,targetLogger);
        }

    }
    /**
     * 自定义的任务
     */
    public abstract void execute();

    @Override
    public void run() {
        // TODO 统计超时任务
        stopWatch.stop();
        long delayTime = stopWatch.getNanoTime();
        stopWatch.reset();
        stopWatch.start();
        try{
            execute();
        }catch (Throwable e){
            logger.error("任务执行失败，抛出异常",e);
        }finally {
            stopWatch.stop();
            long executeTime = stopWatch.getNanoTime();
            if (this.logOrNot()){
                log(delayTime,executeTime);
            }
        }
    }
}
