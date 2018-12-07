package com.mmorpg.mbdl.framework.communicate.websocket.handler;

import com.mmorpg.mbdl.bussiness.common.PacketIdManager;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import com.mmorpg.mbdl.framework.communicate.websocket.model.WsPacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@ChannelHandler.Sharable
@Component
public class AbstractPacketOutboundHandler extends ChannelOutboundHandlerAdapter {
    private static final Logger logger= LoggerFactory.getLogger(AbstractPacketOutboundHandler.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof AbstractPacket) {
            try{
                PacketIdManager packetIdManager = PacketIdManager.getInstance();
                short packetId = packetIdManager.getPacketId(msg.getClass());
                super.write(ctx, WsPacket.valueOf(packetId,packetIdManager.getCodec(packetId).encode(msg)), promise);
            }catch (IOException e){
                logger.error("消息包[{}]编码失败",msg.getClass().getSimpleName());
            }
        }else {
            // 其它响应消息直接往上传
            // logger.info(""+msg);
            super.write(ctx, msg, promise);
        }
    }
}
