package com.mmorpg.mbdl.framework.resource.facade;

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
     * 解析逻辑编实现在这个函数中
     */
    void resolve();
}
