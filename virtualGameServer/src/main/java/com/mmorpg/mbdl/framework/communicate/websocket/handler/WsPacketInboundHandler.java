package com.mmorpg.mbdl.framework.communicate.websocket.handler;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import com.mmorpg.mbdl.framework.communicate.websocket.model.SessionManager;
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
    @Autowired
    private SessionManager sessionManager;

    private static WsPacketInboundHandler instance;
    public static WsPacketInboundHandler getInstance(){return instance;}

    @PostConstruct
    private void init(){
        WsPacketInboundHandler.instance=this;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WsPacket wsPacket) throws Exception {
        short packetId = wsPacket.getPacketId();
        // logger.debug(String.format("packetId=%s", packetId));
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
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            logger.debug(String.format("channel[%s]握手成功",ctx.channel().remoteAddress().toString()));
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }
}