package com.mmorpg.nb.framework.communicate.socket.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SocketClientHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(SocketClientHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
        // TODO Auto-generated method stub
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        String data = msg.toString();
        logger.info("数据内容：data="+data);

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String data) throws Exception {

    }
}

