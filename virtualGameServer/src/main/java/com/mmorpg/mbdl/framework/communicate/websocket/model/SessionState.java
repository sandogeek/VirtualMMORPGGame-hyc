package com.mmorpg.mbdl.framework.communicate.websocket.model;

/**
 * 会话状态
 * 根据状态过滤请求，例如很多请求在未登录时都不需要处理。
 * @since 1.0
 * @author sando
 */
public enum SessionState {
    /**
     * 表示请求在任何状态下都会处理，例如心跳
     */
    ANY,
    /**
     * 表示请求在刚刚连接状态才会处理，例如登录
     */
    CONNECTED,
    /**
     * 已经登录，但是还没有进入游戏，例如角色选择请求必须处于这种状态
     */
    LOGINED,
    /**
     * 表示请求只有处在游戏中才会处理<br/>
     * 这属于大部分情况，所以作为默认设置
     */
    GAMEING
}
