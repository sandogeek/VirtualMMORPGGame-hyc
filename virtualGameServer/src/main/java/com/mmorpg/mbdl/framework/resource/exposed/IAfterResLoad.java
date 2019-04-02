package com.mmorpg.mbdl.framework.resource.exposed;

/**
 * 资源对象生成后调用
 *
 * @author Sando Geek
 * @since v1.0 2019/3/29
 **/
public interface IAfterResLoad {
    /**
     * 资源对象生成后调用
     */
    void afterLoad();
}
