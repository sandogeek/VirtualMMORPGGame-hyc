package com.mmorpg.mbdl.framework.resource.core;

import org.springframework.core.io.Resource;

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

    /** 资源类.class对象 */
    private Class<?> clazz;
    private Resource resource;
    // 额外的信息
    private Map<String,Object> extra;

    // /** id字段 */
    // private Field idField;
    // private Map<String,Field> uniqueFieldName2Field;
    // private Map<String,Field> indexFieldName2Field;
    //
    // /** 动态生成的IStaticRes接口的子类 */
    // private Class<? extends IStaticRes>

    public StaticResDefinition(Class<?> clazz,Map<String,Object> extra) {
        /*init(clazz);*/
        this.clazz = clazz;
        this.extra = extra;
    }

    // /**
    //  * 初始化uniqueFieldName2Field，indexFieldName2Field
    //  * @param clazz
    //  */
    // private void init(Class<?> clazz){
    //
    // }

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

    public Map<String, Object> getExtra() {
        return extra;
    }
}
