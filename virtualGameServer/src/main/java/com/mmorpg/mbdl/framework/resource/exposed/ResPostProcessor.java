package com.mmorpg.mbdl.framework.resource.exposed;

/**
 * 静态资源对象后处理器
 *
 * @author Sando Geek
 * @since v1.0 2019/4/2
 **/
public interface ResPostProcessor {
    /**
     * 对解析出来的每个静态资源对象进行后处理
     *
     * @param obj
     */
    void postProcess(Object obj);
}
