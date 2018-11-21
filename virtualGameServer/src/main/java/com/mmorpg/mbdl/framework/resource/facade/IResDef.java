package com.mmorpg.mbdl.framework.resource.facade;

/**
 * 资源定义接口
 *
 * @author Sando Geek
 * @since v1.0
 **/
public interface IResDef<K> {
    /**
     * 获取资源主键
     * @return 资源主键类型的值
     */
    K getId();
}
