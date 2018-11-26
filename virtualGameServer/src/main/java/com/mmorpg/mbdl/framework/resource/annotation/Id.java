package com.mmorpg.mbdl.framework.resource.annotation;

import java.lang.annotation.*;

/**
 * 标识某字段为静态资源主键
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Id {
}
