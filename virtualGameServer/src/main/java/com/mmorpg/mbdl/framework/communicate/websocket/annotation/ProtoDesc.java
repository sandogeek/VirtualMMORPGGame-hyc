package com.mmorpg.mbdl.framework.communicate.websocket.annotation;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ProtoDesc {
    /**
     * @return description to the field
     */
    String description() default "";
}
