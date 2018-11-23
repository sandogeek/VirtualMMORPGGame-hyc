package com.mmorpg.mbdl.framework.resource.annotation;

import java.lang.annotation.*;

/**
 * 资源类定义接口
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResDef {
    /** 资源文件名，如果为空，那么资源文件名为类的getSimpleName */
    String value() default "";
}
