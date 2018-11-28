package com.mmorpg.mbdl.framework.resource.annotation;

import java.lang.annotation.*;

/**
 * 资源类定义接口
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResDef {
    /**
     * 资源路径名不带后缀，如果为空，那么资源文件名为类的getSimpleName
     * <p>支持Ant风格路径，如果填写多个路径，或者路径匹配多个文件，必须设置{@link ResDef#isTargetSingle()}为false</p>
     * <p>生成的最终文件名默认添加前缀<code>classpath*:&#42;&#42;/</code>,所以定制的文件名如果仅有一份，只写文件名即可</p>
     */
    String[] value() default {""};

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

    /**
     * 当前类是否对应单个Resource文件，默认true
     * @return 单个true,多个false
     */
    boolean isTargetSingle() default true;
}
