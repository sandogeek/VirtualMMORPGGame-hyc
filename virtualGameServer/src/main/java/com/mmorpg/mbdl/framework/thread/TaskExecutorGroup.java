package com.mmorpg.mbdl.framework.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 任务线程池组
 */
public class TaskExecutorGroup {
    private static final Logger logger = LoggerFactory.getLogger(TaskExecutorGroup.class);
    /**
     * 线程池数量，默认是cup内核数+4
     * TODO 放到配置文件中，支持注解方式写到此变量中
     */
    private static int EXECUTOR_SIZE = Runtime.getRuntime().availableProcessors()+4;

    // 线程池组
    private static ScheduledThreadPoolExecutor[] executorGroup;

    public static void init(){
        init(EXECUTOR_SIZE);
    }
    public static void init(int executorSize){
        init(executorSize,"-");
    }
    public static void init(int executorSize,String threadNamePrefix){
        EXECUTOR_SIZE = executorSize;
        if (executorGroup == null){
            executorGroup = new ScheduledThreadPoolExecutor[executorSize];
            for (int i = 0; i < executorGroup.length; i++) {
                // TODO 由于ScheduledThreadPoolExecutor内部使用无界队列，因为可能导致OOM，因而需要定制
                executorGroup[i] = new ScheduledThreadPoolExecutor(1,new CustomizableThreadFactory("池-"+i+threadNamePrefix));
            }
        }
    }

    /**
     * 根据dispatcherId获取线程池
     * @param dispatcherId 分配器id，通常定义在{@link AbstractTask#getDispatcherId()}
     * @return 线程池
     */
    private static ScheduledThreadPoolExecutor getExecutor(int dispatcherId) {
        return executorGroup[Math.abs(dispatcherId)%EXECUTOR_SIZE];
    }

    /**
     * 添加同步任务
     * @param runnable 任务
     * @return ScheduledFuture可用于控制任务以及检查状态
     */
    public static Future<?> addTask(AbstractTask runnable) {
        return getExecutor(runnable.getDispatcherId()).submit(runnable);
    }

    /**
     * {@link TaskExecutorGroup#addDelayedTask(AbstractTask, long, TimeUnit)}设置延时任务默认时间单位为毫秒
     */
    public static ScheduledFuture<?> addDelayedTask(AbstractTask runnable, long delay){
        return addDelayedTask(runnable,delay,TimeUnit.MILLISECONDS);
    }
    /**
     * 添加延时执行任务
     * @param runnable 任务
     * @param delay 延迟时间
     * @param timeUnit 使用的时间单位
     * @return ScheduledFuture可用于控制任务以及检查状态
     */
    public static ScheduledFuture<?> addDelayedTask(AbstractTask runnable, long delay, TimeUnit timeUnit) {
        return getExecutor(runnable.getDispatcherId()).schedule(runnable,delay,timeUnit);
    }

    /**
     * {@link TaskExecutorGroup#addFixedRateTask(AbstractTask, long, long)}设置延时任务默认时间单位为毫秒
     */
    public static ScheduledFuture<?> addFixedRateTask(AbstractTask runnable, long initalDelay, long period){
        return addFixedRateTask(runnable,initalDelay,period,TimeUnit.MILLISECONDS);
    }
    /**
     * 添加固定时间周期执行的任务
     * @param runnable AbstractDispatcherRunnable类型的任务
     * @param initalDelay 初始化延迟
     * @param period 周期时间
     * @param timeUnit 时间单位
     * @return ScheduledFuture可用于控制任务以及检查状态
     */
    public static ScheduledFuture<?> addFixedRateTask(AbstractTask runnable, long initalDelay, long period, TimeUnit timeUnit) {
        return getExecutor(runnable.getDispatcherId()).scheduleAtFixedRate(runnable,initalDelay,period,timeUnit);
    }

    /**
     * 关闭各个线程池
     */
    public static void shutdown() {
        for (ScheduledThreadPoolExecutor scheduledThreadPoolExecutor:
             executorGroup) {
            scheduledThreadPoolExecutor.shutdown();
        }
    }
}
