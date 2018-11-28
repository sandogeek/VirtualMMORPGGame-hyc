package com.mmorpg.mbdl.framework.resource.annotation;

/**
 * 标明表格型资源属性名，配合{@link ResDef}使用
 * @author Sando Geek
 */
public @interface Attr {
    /**
     * 字段对应的资源属性名，默认以字段名作为属性名
     * @return 资源属性名
     */
    String value() default "";
}
