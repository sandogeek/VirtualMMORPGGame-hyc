package com.mmorpg.mbdl.framework.resource.exposed;

import com.mmorpg.mbdl.framework.resource.core.StaticResDefinition;

import java.util.List;

/**
 * 静态资源解析接口
 *
 * @author Sando Geek
 * @since v1.0
 **/
public interface IResResolver {
    /**
     * 解析的静态资源文件后缀名
     * @return
     */
    String suffix();
    /**
     * 解析逻辑编实现在这个函数中,注意这个函数是并发调用的，需要注意避免并发问题
     * @param staticResDefinition 静态资源定义类
     * @return 解析好的资源对象列表
     */
    List<Object> resolve(StaticResDefinition staticResDefinition);
}
