package com.mmorpg.mbdl.framework.resource.core;

import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
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
    /** 待注册到容器中的IStaticRes子类的单例Bean */
    private IStaticRes iStaticRes;
    private ConfigurableListableBeanFactory beanFactory;

    /**
     * TODO 初始化uniqueFieldName2Field，indexFieldName2Field
     * @param vClass
     */
    private void init(Class<?> vClass){
        if (idField==null){
            // idField==null说明不是表格型资源
            return;
        }else {
            idField.setAccessible(true);
        }
    }

    // public Object getIdFieldValue(Object obj){
    //     return fieldAccess.getObject(obj,idFieldIndex);
    // }

    public String getFullFileName() {
        return fullFileName;
    }

    public Field getIdField() {
        return idField;
    }

    public void setIdField(Field idField) {
        this.idField = idField;
    }

    public Class<?> getvClass() {
        return vClass;
    }

    public void setvClass(Class<?> vClass) {
        init(vClass);
        fullFileName = IStaticResUtil.getFullFileName(vClass);
        this.vClass = vClass;
    }

    public void setStaticRes(StaticRes staticRes) {
        this.staticRes = staticRes;
    }

    public void setiStaticRes(IStaticRes iStaticRes) {
        this.iStaticRes = iStaticRes;
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
        String resBeanName = StringUtils.uncapitalize(iStaticRes.getClass().getSimpleName());
        beanFactory.registerSingleton(resBeanName, iStaticRes);
        return immutableMap;
    }

    @CanIgnoreReturnValue
    public ImmutableMap.Builder add(Object value) {
        try {
            Object key = idField.get(value);
            key2ResourceBuilder.put(key, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return key2ResourceBuilder;
    }
}
