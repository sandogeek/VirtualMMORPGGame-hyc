package com.mmorpg.mbdl.framework.resource.exposed;

/**
 * 静态资源对象后处理器
 *
 * @author Sando Geek
 * @since v1.0 2019/4/2
 **/
public interface ResPostProcessor {
    /**
     * 对解析出来的每个静态资源对象进行后处理<br/>
     * 注意：这个方法是并发运行的（但obj.getClass()相同的类运行在同一线程中），注意规避并发问题
     *
     * @param obj
     */
    void postProcess(Object obj);

    /**
     * 优先级，数值越大越先被应用到静态资源对象上
     *
     * @return
     */
    default int order() {
        return Integer.MIN_VALUE;
    }
}
