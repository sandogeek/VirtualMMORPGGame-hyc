package com.mmorpg.mbdl.framework.storage.annotation;

import java.lang.annotation.*;

/**
 * 标识一个类是由ByteBuddy生成的
 * @author sando
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ByteBuddyGenerated {
}
