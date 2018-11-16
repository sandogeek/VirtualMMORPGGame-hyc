package com.mmorpg.mbdl.framework.storage.annotation;

import java.lang.annotation.*;

/**
 * 标识一个类是由ByteBuddy生成的
 * 暂时采用自行配置接口的方式
 * @author sando
 */
@Deprecated
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ByteBuddyGenerated {
}
