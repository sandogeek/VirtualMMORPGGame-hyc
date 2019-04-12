package com.mmorpg.mbdl.framework.resource.core;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 静态资源定义管理器
 *
 * @author Sando Geek
 * @since v1.0
 **/
@Component
public class StaticResDefinitionFactory {
    /**
     * 为了实现遍历一次目录就完成向StaticResDefinition注入对应的Resource使用了这个数据结构
     * fullFileName -> StaticResDefinition
     */
    private Map<String, StaticResDefinition> fullFileName2StaticResDefinition = new ConcurrentHashMap<>(32);

    public Map<String, StaticResDefinition> getFullFileName2StaticResDefinition() {
        return fullFileName2StaticResDefinition;
    }

}
