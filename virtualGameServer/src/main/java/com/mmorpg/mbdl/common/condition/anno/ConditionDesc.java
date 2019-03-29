package com.mmorpg.mbdl.common.condition.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 条件描述，用于生成条件相关文档
 *
 * @author Sando Geek
 * @since v1.0 2019/3/29
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionDesc {
    String value();
}
