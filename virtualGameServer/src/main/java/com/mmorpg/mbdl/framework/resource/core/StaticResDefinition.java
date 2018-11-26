package com.mmorpg.mbdl.framework.resource.core;

import org.springframework.core.io.Resource;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 静态资源定义
 *
 * @author Sando Geek
 * @since v1.0
 **/
public class StaticResDefinition {
    // /**  */
    // private String fullFileName;

    /** 资源类对象 */
    private Class<?> clazz;
    private Resource resource;

    /** id字段 */
    private Field idField;
    private Map<String,Field> uniqueFieldName2Field;
    private Map<String,Field> indexFieldName2Field;

    public StaticResDefinition(Class<?> clazz) {
        // TODO 初始化其余信息
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Field getIdField() {
        return idField;
    }

    public void setIdField(Field idField) {
        this.idField = idField;
    }

    public Map<String, Field> getUniqueFieldName2Field() {
        return uniqueFieldName2Field;
    }

    public void setUniqueFieldName2Field(Map<String, Field> uniqueFieldName2Field) {
        this.uniqueFieldName2Field = uniqueFieldName2Field;
    }

    public Map<String, Field> getIndexFieldName2Field() {
        return indexFieldName2Field;
    }

    public void setIndexFieldName2Field(Map<String, Field> indexFieldName2Field) {
        this.indexFieldName2Field = indexFieldName2Field;
    }
}
