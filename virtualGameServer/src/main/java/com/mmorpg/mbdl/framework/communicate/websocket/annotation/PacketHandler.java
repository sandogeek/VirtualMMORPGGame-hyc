package com.mmorpg.mbdl.framework.communicate.websocket.annotation;

import java.lang.annotation.*;

/**
 * 标识请求包处理类
 * @author sando
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PacketHandler {
}
