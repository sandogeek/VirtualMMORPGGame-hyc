package com.mmorpg.mbdl.framework.communicate.websocket.handler;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WsPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 将WsPacket字节包转化为AbstractPacket
 * @author sando
 */
@ChannelHandler.Sharable
@Component
public class WsPacketInboundHandler extends SimpleChannelInboundHandler<WsPacket> {
    private static final Logger logger= LoggerFactory.getLogger(WsPacketInboundHandler.class);

    @Autowired
    private PacketIdManager packetIdManager;

    private static WsPacketInboundHandler instance;
    public static WsPacketInboundHandler getInstance(){return instance;}

    @PostConstruct
    private void init(){
        WsPacketInboundHandler.instance=this;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WsPacket wsPacket) throws Exception {
        short packetId = wsPacket.getPacketId();
        logger.debug(String.format("packetIdManager=%s", packetId));
        // 接下来将WsPacket里面的byte[] data转化为AbstractPacket对象，因而
        // 一个<packetIdManager->protobuf编解码代理对象>的map
        Codec codec = this.packetIdManager.getCodec(packetId);
        if (codec == null)  {
            logger.warn(String.format("客户端传来的packetid[%s]不存在", packetId));
            return;
        }
        Object abstractPacket = codec.decode(wsPacket.getData());
        if (abstractPacket instanceof AbstractPacket){
            // 把AbstractPacket对象往pineline后面传递
            ctx.fireChannelRead(abstractPacket);
        }

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