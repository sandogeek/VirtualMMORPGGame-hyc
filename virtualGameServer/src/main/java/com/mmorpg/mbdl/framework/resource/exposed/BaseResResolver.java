package com.mmorpg.mbdl.framework.resource.exposed;

import com.mmorpg.mbdl.framework.resource.core.StaticResDefinition;

/**
 * 静态资源解析基类
 *
 * @author Sando Geek
 * @since v1.0
 **/
public abstract class BaseResResolver {
    /**
     * 解析的静态资源文件后缀名
     * @return
     */
    public abstract String suffix();
    /**
     * 解析逻辑编实现在这个函数中,注意这个函数是并发调用的，需要注意避免并发问题
     * @param staticResDefinition 静态资源定义类
     * @return 解析好的资源对象列表
     */
    public abstract void resolve(StaticResDefinition staticResDefinition);
}
