package com.mmorpg.mbdl.framework.communicate.websocket.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@Component
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            // 限定时间内没有接收到请求，关闭连接
            ctx.close();
            // 指定时间内channel没有读取（客户端没有发请求），向客户端发送ping心跳确认客户端是否在线
            // ctx.writeAndFlush(new PingHeartBeat())
            //         .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
