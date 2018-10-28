package com.mmorpg.mbdl.framework.communicate.websocket.model;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * 收到第一个包时生成一个会话
 * @author sando
 */
public class WsSession extends AbstractSession {
    private Logger logger = LoggerFactory.getLogger(WsSession.class);
    private Channel channel;
    private String playerId;

    public WsSession(Channel channel) {
        super(channel.id(), ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress());
        this.channel = channel;
    }

    @Override
    public ChannelFuture sendPacket(AbstractPacket abstractPacket) {
        if (!channel.isActive()) {
            logger.warn("发包失败：发包时channel={}已inActive, playerId={}", channel, playerId);
            return null;
        }
        return channel.writeAndFlush(abstractPacket);
    }

    @Override
    public String getPlayerId() {
        return playerId;
    }

    /**
     * 设置玩家playerId
     * @param playerId 玩家id
     */
    public void setPlayerId(String playerId){
        this.playerId = playerId;
    }
}
