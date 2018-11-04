package com.mmorpg.mbdl.framework.communicate.websocket.model;

import com.mmorpg.mbdl.framework.event.core.SyncEventBus;
import com.mmorpg.mbdl.framework.event.preset.SessionCloseEvent;
import com.mmorpg.mbdl.framework.thread.task.DelayedTask;
import com.mmorpg.mbdl.framework.thread.task.TaskDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 收到第一个包时生成一个会话
 * @author sando
 */
public class WsSession extends AbstractSession {
    private Logger logger = LoggerFactory.getLogger(WsSession.class);
    private Channel channel;
    private Long playerId;
    // 是否生成新的DelayedTask
    private static AtomicBoolean genereteDelayedTask = new AtomicBoolean(true);

    public WsSession(Channel channel) {
        super(channel.id(), ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress());
        this.channel = channel;
    }

    @Override
    public ChannelFuture sendPacket(AbstractPacket abstractPacket) {
        return sendPacket(abstractPacket,false);
    }

    @Override
    public ChannelFuture sendPacket(AbstractPacket abstractPacket,boolean flushNow){
        if (!channel.isActive()) {
            logger.warn("发包失败：发包时channel={}已inActive, playerId={}", channel, playerId);
            return null;
        }
        if (flushNow){
            return channel.writeAndFlush(abstractPacket);
        }
        ChannelFuture future = channel.write(abstractPacket);
        if (genereteDelayedTask.compareAndSet(true,false)){
            // 缓冲25毫秒
            TaskDispatcher.getIntance().dispatch(new DelayedTask(25, TimeUnit.MILLISECONDS) {
                // 因为直接丢到线程池，没有放入队列，所以不需要dispatcherId
                @Override
                public Serializable getDispatcherId() {
                    return null;
                }

                @Override
                public String taskName() {
                    return "发包任务（带缓冲）";
                }

                @Override
                public void execute() {
                    genereteDelayedTask.compareAndSet(false,true);
                    channel.flush();
                }

                @Override
                public boolean isLogOrNot() {
                    return false;
                }
            },true);
        }
        return future;
    }

    @Override
    public void close() {
        ChannelId channelId = channel.id();
        SyncEventBus.getInstance().post(new WsSession(channel));
        channel.close().addListener( future -> {
            SyncEventBus.getInstance().post(new SessionCloseEvent(channelId));
            // logger.info("SessionCloseEvent post成功");
        } );
    }

    @Override
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * 设置玩家playerId
     * @param playerId 玩家id
     */
    public void setPlayerId(Long playerId){
        this.playerId = playerId;
    }
}
