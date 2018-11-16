package com.mmorpg.mbdl.framework.storage.annotation;

import com.github.xiaolyuh.annotation.FirstCache;
import com.github.xiaolyuh.annotation.SecondaryCache;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 缓存配置
 * @author Sando Geek
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheConfig {
    /**
     * 缓存名称,当其为默认的空字符串时，缓存名称为此注解所在的类的类名
     * @return String
     */
    @AliasFor("cacheName")
    String value() default "";
    @AliasFor("value")
    String cacheName() default "";

    /**
     * 描述，用于统计信息时显示
     *
     * @return String
     */
    String depict() default "";

    /**
     * 是否忽略在操作缓存中遇到的异常，如反序列化异常，默认true。
     * <p>true: 有异常会输出warn级别的日志，并直接执行被缓存的方法（缓存将失效）</p>
     * <p>false:有异常会输出error级别的日志，并抛出异常</p>
     *
     * @return boolean
     */
    boolean ignoreException() default true;

    /**
     * 一级缓存配置
     *
     * @return FirstCache
     */
    FirstCache firstCache() default @FirstCache();

    /**
     * 二级缓存配置
     *
     * @return SecondaryCache
     */
    SecondaryCache secondaryCache() default @SecondaryCache();
}
