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

    /**
     * 资源文件后缀名
     * @return 后缀名
     */
    String getSuffix() default ".xlsx";

    /**
     * 是否是表格型的资源
     * <p>表格型的资源都可以用IStaticRes接口访问，必须具有@Id标注的字段，可以在字段上添加@Index,@Unique注解</p>
     * <p>例如，json,excel,csv，数据库都算是表格型的资源，而properties不算表格型的资源</p>
     * @return
     */
    boolean isTable() default true;
}
