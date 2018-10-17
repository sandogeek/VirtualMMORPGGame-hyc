package com.mmorpg.mbdl.framework.communicate.websocket.codec;

import com.mmorpg.mbdl.framework.communicate.websocket.model.WsPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * WebsocketFrame编解码器
 * @author sando
 */
public class WebSocketFrameToWsPacketCodec extends MessageToMessageCodec<WebSocketFrame, WsPacket> {
    private static final Logger logger= LoggerFactory.getLogger(WebSocketFrameToWsPacketCodec.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, WsPacket msg, List<Object> out) throws Exception {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
        if(!(msg instanceof BinaryWebSocketFrame)){
            throw new RuntimeException(String.format("不支持的WebSocketFrame类型[%s]",msg.getClass()));
        }
        ByteBuf byteBuf=((BinaryWebSocketFrame)msg).content();
        int packetLength = byteBuf.readInt();
        // 检查数据包长度是否与发送时一致，不一致的包直接丢弃
        if ((packetLength-4)!=byteBuf.readableBytes()) {
            logger.error("数据包不完整");
            return;
        }
        logger.debug(String.format("包总字节数=%s", packetLength));
        short packetId = byteBuf.readShort();
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        // byteBuf.retain();
        out.add(WsPacket.valueOf(packetId,data));
    }
}
