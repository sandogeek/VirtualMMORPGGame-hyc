package com.mmorpg.mbdl.framework.thread.task;

import com.mmorpg.mbdl.framework.thread.PoolExecutor;
import com.mmorpg.mbdl.framework.thread.ThreadUtils;
import com.mmorpg.mbdl.framework.thread.interfaces.Dispatchable;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 抽象的任务runnable
 * @author sando
 */
public abstract class AbstractTask<E extends Dispatchable<T>, T extends Serializable> implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private long maxDelay = TimeUnit.NANOSECONDS.convert(2,TimeUnit.MILLISECONDS);
    private long maxExecute = TimeUnit.NANOSECONDS.convert(3,TimeUnit.MILLISECONDS);
    private Logger targetLogger = logger;
    private E dispatcher;
    private boolean executeParallel = false;
    private TaskQueue<T> taskQueue;
    private PoolExecutor<T, ? extends ScheduledExecutorService> executor;


    public AbstractTask(E dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * 是否打印日志
     */
    private boolean logOrNot = true;
    private StopWatch stopWatch = new StopWatch();
    {
        // 创建时开始计时
        stopWatch.start();
    }

    /**
     * 获取分发id（用于submit前获取队列）
     * 必须保证唯一性，不能是hashcode因为有可能有hash冲突，导致不同的玩家或者Channel使用同一TaskQueue
     * @return
     */
    public E getDispatcher(){
        return dispatcher;
    }

    public boolean isExecuteParallel() {
        return executeParallel;
    }

    public void setExecuteParallel(boolean executeParallel){
        this.executeParallel = executeParallel;
    }

    /**
     * 设置任务名称
     * @return taskName
     */
    public abstract String taskName();

    /**
     * 任务延迟过长打印
     */
    protected void logIfOverDelay(long delayTime,long executeTime,Logger targetLogger){
        targetLogger.warn("任务:{}，执行延时超出预期({}ms)，delay={}ms,execute={}ms",this.taskName(), getMaxDelayTime()/100_0000F,delayTime/1000000F,executeTime/1000000F);
    }
    /**
     * 任务执行时间过长打印
     */
    protected void logIfOverExecute(long delayTime,long executeTime,Logger targetLogger){
        targetLogger.warn("任务:{}，执行时间超出预期({}ms)，delay={}ms,execute={}ms",this.taskName(), getMaxExecuteTime()/100_0000F,delayTime/1000000F,executeTime/1000000F);
    }
    protected void logIfNormal(long delayTime,long executeTime,Logger targetLogger){
        targetLogger.info("任务:{},delay={}ms,execute={}ms",this.taskName(),delayTime/100_0000F,executeTime/100_0000F);
    }
    /**
     * 打印日志
     */
    private void log(long delayTime,long executeTime){
        long maxDelayTime = this.getMaxDelayTime();
        long maxExecuteTime = this.getMaxExecuteTime();
        Logger targetLogger = this.getTargetLogger();
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
        ThreadUtils.setCurrentThreadTask(this);
        stopWatch.start();
        try{
            beforeExecute();
        }catch (Throwable e){
            logger.error("[{}] 任务:{}执行失败，抛出异常", dispatcher, taskName(), e);
        }finally {
            stopWatch.stop();
            long executeTime = stopWatch.getNanoTime();
            if (this.isLogOrNot()){
                log(delayTime,executeTime);
            }
            ThreadUtils.removeCurrentThreadTask();
            // 不是并行执行的情况下才会把队列下一个任务加入线程池
            if (!isExecuteParallel()){
                getTaskQueue().andThen();
            }
        }
    }

    /**
     * 定义真正执行前的一些处理
     */
    protected void beforeExecute() {
        execute();
    }
    /**
     * 最大延迟时间,默认2毫秒
     */
    public long getMaxDelayTime() {
        return maxDelay;
    }

    public AbstractTask setMaxDelayTime(long maxDelay, TimeUnit timeUnit) {
        this.maxDelay = TimeUnit.NANOSECONDS.convert(maxExecute,timeUnit);
        return this;
    }
    /**
     * 最大延迟时间,默认3毫秒
     */
    public long getMaxExecuteTime() {
        return maxExecute;
    }

    public AbstractTask<E, T> setMaxExecuteTime(long maxExecute, TimeUnit timeUnit) {
        this.maxExecute = TimeUnit.NANOSECONDS.convert(maxExecute,timeUnit);
        return this;
    }

    /**
     * 使用的logger,可以为不同的任务定制不同的logger
     * @return
     */
    public Logger getTargetLogger() {
        return targetLogger;
    }

    public void setTargetLogger(Logger targetLogger) {
        this.targetLogger = targetLogger;
    }

    protected boolean isLogOrNot() {
        return logOrNot;
    }

    public AbstractTask<E, T> setLogOrNot(boolean logOrNot) {
        this.logOrNot = logOrNot;
        return this;
    }

    public void setTaskQueue(TaskQueue<T> taskQueue) {
        this.taskQueue = taskQueue;
    }

    public TaskQueue<T> getTaskQueue() {
        return taskQueue;
    }

    public void setExecutor(PoolExecutor<T, ? extends ScheduledExecutorService> executor) {
        this.executor = executor;
    }

    public PoolExecutor<T, ? extends ScheduledExecutorService> getExecutor() {
        return executor;
    }
}
