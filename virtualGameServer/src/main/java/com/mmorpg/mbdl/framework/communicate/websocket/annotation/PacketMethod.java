package com.mmorpg.mbdl.framework.communicate.websocket.annotation;

import com.mmorpg.mbdl.framework.communicate.websocket.model.SessionState;

import java.lang.annotation.*;

/**
 * 处理某个AbstractPacket的方法<br/>
 * 通常情况可以不加，只有默认配置不满足要求的时候才需要添加，或者同一个类中包含两个处理同一种包的方法时用于标识入口
 * @since v1.0
 * @author sando
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PacketMethod {
    /**
     * 是否打印这种包的日志
     * @return
     */
    boolean logOrNot() default true;
    /**
     * Session处于什么状态时才处理这种包
     */
    SessionState state() default SessionState.GAMEING;
    /**
     * 是否并行执行,默认串行，聊天请求也不可以并行，因为会导致后发的消息可能先出现在其他玩家的消息列表上
     */
    boolean executeParallel() default false;
}
