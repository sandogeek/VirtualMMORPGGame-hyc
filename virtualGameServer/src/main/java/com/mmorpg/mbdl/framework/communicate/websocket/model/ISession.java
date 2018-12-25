package com.mmorpg.mbdl.framework.communicate.websocket.model;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;

import java.io.Serializable;

public interface ISession {
    /**
     * 选择分发器Id
     * @return
     */
    Serializable selectDispatcherId();

    /**
     * 获取Session的id（其实是ChannelId）
     * @return channel().id()
     */
    ChannelId getId();

    /**
     * 获取客户端ip地址
     * @return ip地址
     */
    String getIp();

    /**
     * 获取会话状态
     * @return 会话状态
     */
    SessionState getState();

    /**
     *设置会话状态
     * @param state 会话状态
     */
    void setState(SessionState state);

    /**
     * 发送一个响应包（带缓冲处理）
     * @param abstractPacket 抽象包
     * @return ChannelFuture
     */
    ChannelFuture sendPacket(AbstractPacket abstractPacket);
    /**
     * 立刻发送一个响应包（无缓冲处理）
     * @param abstractPacket 抽象包
     * @param flushNow 是否立即刷新
     * @return ChannelFuture
     */
    ChannelFuture sendPacket(AbstractPacket abstractPacket,boolean flushNow);

    // /**
    //  * 获取上次发送ping包的时间
    //  * @return nano时间
    //  */
    // long getLastSendPing();
    //
    // /**
    //  * 获取上次接收Pong包的时间
    //  * @return
    //  */
    // long getLastRescPong();

    /**
     * 关闭session
     */
    void close();

    // String getUid(); java.rmi.server.UID
    /**
     * 获取角色id
     * @return 角色id
     */
    Long getRoleId();

    /**
     * 获取Channel
     * @return Channel
     */
    Channel getChannel();

    /**
     * 设置角色id
     * @param roleId 角色id
     */
    void setRoleId(Long roleId);

    /**
     * 获取账号
     * @return 玩家账号
     */
    String getAccount();

    /**
     * 设置账号
     */
    void setAccount(String account);
}
