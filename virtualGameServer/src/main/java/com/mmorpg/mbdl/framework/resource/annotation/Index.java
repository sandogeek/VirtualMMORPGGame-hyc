package com.mmorpg.mbdl.framework.resource.annotation;

import java.lang.annotation.*;

/**
 * 标识资源定义类的某个字段为索引字段，配合{@link ResDef}使用
 * @see Unique
 * @see com.mmorpg.mbdl.framework.resource.exposed.IStaticRes#getByIndex(String, Object)
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {
}
