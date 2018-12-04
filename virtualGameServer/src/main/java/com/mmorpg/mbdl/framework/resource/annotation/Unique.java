package com.mmorpg.mbdl.framework.resource.annotation;

import java.lang.annotation.*;

/**
 * 标识资源定义类的某个字段为唯一值字段,配合{@link ResDef}
 * @see Index
 * @see com.mmorpg.mbdl.framework.resource.exposed.IStaticRes#getByUnique(String, Object)
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {
}
