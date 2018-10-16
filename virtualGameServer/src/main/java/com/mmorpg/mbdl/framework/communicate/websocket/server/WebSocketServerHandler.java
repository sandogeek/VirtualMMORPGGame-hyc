package com.mmorpg.mbdl.framework.communicate.websocket.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sando
 */
@ChannelHandler.Sharable
@Component
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private static final Logger logger= LoggerFactory.getLogger(WebSocketServerHandler.class);

    private static WebSocketServerHandler instance;
    public static WebSocketServerHandler getInstance(){return instance;}

    @PostConstruct
    private void init(){
        WebSocketServerHandler.instance=this;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if(!(frame instanceof BinaryWebSocketFrame)){
            throw new RuntimeException(String.format("不支持的WebSocketFrame类型[%s]",frame.getClass()));
        }
        ByteBuf byteBuf=((BinaryWebSocketFrame)frame).content();
        // BinaryWebSocketFrame binaryWebSocketFrame=new BinaryWebSocketFrame(Unpooled.buffer().writeBytes("xxx".getBytes()));
        // ctx.channel().writeAndFlush(binaryWebSocketFrame);
        ctx.fireChannelRead(byteBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("捕获到异常：",cause);
        ctx.close();
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // TODO 如果有多浏览器的需求可以考虑在此处将channel添加到某个数据结构中
        logger.debug("channel成功注册到EventLoop");
    }
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        // TODO 如果有多浏览器的需求可以考虑在此处将channel从某个数据结构中删除
        logger.debug("channel从EventLoop中移除注册");
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            logger.debug(String.format("channel[%s]握手成功",ctx.channel().remoteAddress().toString()));
        }
    }
}