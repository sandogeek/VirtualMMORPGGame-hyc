package com.mmorpg.mbdl.framework.communicate.websocket.model;

import com.mmorpg.mbdl.framework.common.utils.JsonUtil;
import com.mmorpg.mbdl.framework.event.core.SyncEventBus;
import com.mmorpg.mbdl.framework.event.preset.SessionCloseEvent;
import com.mmorpg.mbdl.framework.thread.task.DelayedTask;
import com.mmorpg.mbdl.framework.thread.task.TaskDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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
    private Long roleId;
    private String account;
    /** 临时分发器Id，用于未登录时使用 */
    private Long tempDispatcherId;
    private Long tempDispatcherIdMaxValue;
    /** 是否生成新的DelayedTask */
    private AtomicBoolean generateDelayedTask = new AtomicBoolean(true);

    public WsSession(Channel channel) {
        super(channel.id(), ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress());
        this.channel = channel;
    }

    @Override
    public Serializable selectDispatcherId(){
        if (roleId ==null){
            if (tempDispatcherId ==null){
                // -1 到 -16
                tempDispatcherId = getId().hashCode()% tempDispatcherIdMaxValue - tempDispatcherIdMaxValue;
            }
            return tempDispatcherId;
        }
        return roleId;
    }

    public void setTempDispatcherIdMaxValue(Long tempDispatcherIdMaxValue) {
        this.tempDispatcherIdMaxValue = tempDispatcherIdMaxValue;
    }

    @Override
    public ChannelFuture sendPacket(AbstractPacket abstractPacket) {
        return sendPacket(abstractPacket,false);
    }

    @Override
    public ChannelFuture sendPacket(AbstractPacket abstractPacket,boolean flushNow){
        if (!channel.isActive()) {
            logger.warn("发包失败：发包时channel={}已inActive, roleId={}", channel, roleId);
            return null;
        }
        ChannelFuture future;
        if (flushNow){
            future = channel.writeAndFlush(abstractPacket);
            return future;
        }
        future = channel.write(abstractPacket);
        future.addListener(futureTemp -> {
            String logContent = String.format("账号<%s>,发包[%s] 内容：%s",account, abstractPacket.getClass().getSimpleName(), JsonUtil.object2String(abstractPacket));
            if (futureTemp.isSuccess()) {
                logger.debug(logContent);
            } else if (futureTemp.cause() != null) {
                logger.error(logContent + "失败!", futureTemp.cause());
            }
        });
        if (generateDelayedTask.compareAndSet(true,false)){
            // 缓冲25毫秒
            TaskDispatcher.getInstance().dispatch(new DelayedTask(null,25, TimeUnit.MILLISECONDS) {

                @Override
                public String taskName() {
                    return "发包任务（带缓冲）";
                }

                @Override
                public void execute() {
                    channel.flush();
                    generateDelayedTask.set(true);
                }
            }.setLogOrNot(false),true);
        }
        return future;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public void close() {
        channel.close().addListener( future -> {
            SyncEventBus.getInstance().post(new SessionCloseEvent(this));
            // logger.info("SessionCloseEvent post成功");
        } );
    }

    @Override
    public Long getRoleId() {
        return roleId;
    }

    /**
     * 设置玩家playerId
     * @param roleId 玩家id
     */
    @Override
    public void setRoleId(Long roleId){
        this.roleId = roleId;
    }

    @Override
    public String getAccount() {
        return account;
    }

    @Override
    public void setAccount(String account) {
        this.account = account;
    }
}
