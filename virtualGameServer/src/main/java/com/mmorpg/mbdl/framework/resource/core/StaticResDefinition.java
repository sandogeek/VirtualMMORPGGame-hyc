package com.mmorpg.mbdl.framework.resource.core;

import com.mmorpg.mbdl.framework.resource.impl.StaticRes;
import org.springframework.core.io.Resource;

import java.lang.reflect.Field;
import java.util.List;
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
    private List<Resource> resources;

    /** id字段 */
    private Field idField;
    private Map<String,Field> uniqueFieldName2Field;
    private Map<String,Field> indexFieldName2Field;
    /** 待注册到容器中的IStaticRes子类的单例Bean */
    private StaticRes staticRes;

    /**
     * TODO 初始化uniqueFieldName2Field，indexFieldName2Field
     * @param vClass
     */
    private void init(Class<?> vClass){

    }

    public String getFullFileName() {
        return fullFileName;
    }

    public void setIdField(Field idField) {
        this.idField = idField;
    }

    public void setvClass(Class<?> vClass) {
        init(vClass);
        fullFileName = IStaticResUtil.getFullFileName(vClass);
        this.vClass = vClass;
    }

    public void setStaticRes(StaticRes staticRes) {
        this.staticRes = staticRes;
    }

    public List<Resource> getResources() {
        return resources;
    }

}
