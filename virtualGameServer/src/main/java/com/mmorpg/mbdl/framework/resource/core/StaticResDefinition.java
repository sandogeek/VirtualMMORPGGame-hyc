package com.mmorpg.mbdl.framework.resource.core;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mmorpg.mbdl.framework.resource.impl.StaticRes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
    /** 资源文件全路径名 */
    private String fullFileName;

    /** V的实际类型 */
    private Class<?> vClass;

    private Resource resource;

    /** id字段 */
    private Field idField;
    private Map<String,Field> uniqueFieldName2Field;
    private Map<String,Field> indexFieldName2Field;
    /**
     * 实际存储静态资源数据的对象
     */
    private StaticRes staticRes;
    private ImmutableMap.Builder key2ResourceBuilder = ImmutableMap.builder();
    private ConfigurableListableBeanFactory beanFactory;

    public StaticResDefinition(Class<?> vClass) {
        fullFileName = IStaticResUtil.getFullFileName(vClass);
        this.vClass = vClass;
    }

    public String getFullFileName() {
        return fullFileName;
    }

    public Field getIdField() {
        return idField;
    }

    public void setIdField(Field idField) {
        this.idField = idField;
        idField.setAccessible(true);
    }

    public Class<?> getvClass() {
        return vClass;
    }

    public void setStaticRes(StaticRes staticRes) {
        this.staticRes = staticRes;
    }

    public void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public ImmutableMap finish() {
        ImmutableMap immutableMap = key2ResourceBuilder.build();
        staticRes.setKey2Resource(immutableMap);
        String resBeanName = StringUtils.uncapitalize(staticRes.getClass().getSimpleName());
        beanFactory.registerSingleton(resBeanName, staticRes);
        return immutableMap;
    }

    @CanIgnoreReturnValue
    public ImmutableMap.Builder add(Object value) {
        if (value.getClass() != vClass) {
            throw new RuntimeException(String.format("添加的对象类型错误，%s的%s添加了类型为%s的对象", fullFileName, StaticResDefinition.class.getSimpleName(), value.getClass().getSimpleName()));
        }
        try {
            Object key = idField.get(value);
            key2ResourceBuilder.put(key, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return key2ResourceBuilder;
    }
}
