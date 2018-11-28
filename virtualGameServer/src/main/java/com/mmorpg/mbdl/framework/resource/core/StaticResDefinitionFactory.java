package com.mmorpg.mbdl.framework.resource.core;

import com.google.common.collect.Table;
import org.springframework.stereotype.Component;

/**
 * 静态资源定义管理器
 *
 * @author Sando Geek
 * @since v1.0
 **/
@Component
public class StaticResDefinitionFactory {
    /**
     * fileName -> suffix -> StaticResDefinition
     */
    private Table<String,String,StaticResDefinition> fileNameSuffix2StaticResDefinitionTable;

    public Table<String, String, StaticResDefinition> getFileNameSuffix2StaticResDefinitionTable() {
        return fileNameSuffix2StaticResDefinitionTable;
    }

    public void setFileNameSuffix2StaticResDefinitionTable(Table<String, String, StaticResDefinition> fileNameSuffix2StaticResDefinitionTable) {
        this.fileNameSuffix2StaticResDefinitionTable = fileNameSuffix2StaticResDefinitionTable;
    }
}
