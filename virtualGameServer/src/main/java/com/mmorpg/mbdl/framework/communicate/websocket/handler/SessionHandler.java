package com.mmorpg.mbdl.framework.communicate.websocket.handler;

import com.mmorpg.mbdl.framework.communicate.websocket.model.SessionManager;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WsSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@Component
public class SessionHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SessionHandler.class);
    @Value("${server.config.tempDispatcherIdMaxValue}")
    private Long tempDispaterIdMaxValue;
    @Autowired
    private SessionManager sessionManager;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("捕获到异常：",cause);
        sessionManager.getSession(ctx.channel().id()).close();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        WsSession wsSession = new WsSession(ctx.channel());
        wsSession.setTempDispatcherIdMaxValue(tempDispaterIdMaxValue);
        sessionManager.add(wsSession);
        logger.debug("会话[channelId={}]创建成功",ctx.channel().id());
        super.channelActive(ctx);
    }
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        sessionManager.getSession(ctx.channel().id()).close();
        logger.debug("会话[channelId={}]关闭",ctx.channel().id());
    }
}
