package com.mmorpg.mbdl.framework.resource.annotation;

import java.lang.annotation.*;

/**
 * 标识某字段为静态资源主键,
 * {@link com.mmorpg.mbdl.framework.resource.exposed.IStaticRes#get(Object)}以@Id标注的字段作为主键获取V类型的对象
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
}
