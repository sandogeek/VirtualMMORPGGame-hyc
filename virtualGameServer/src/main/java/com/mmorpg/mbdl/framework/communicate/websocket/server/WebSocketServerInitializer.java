package com.mmorpg.mbdl.framework.communicate.websocket.server;

import com.mmorpg.mbdl.framework.communicate.websocket.codec.WebSocketFrameToWsPacketCodec;
import com.mmorpg.mbdl.framework.communicate.websocket.handler.AbstractPacketDispacherHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.handler.AbstractPacketOutboundHandler;
import com.mmorpg.mbdl.framework.communicate.websocket.handler.WsPacketInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ChannelInitializer
 * @author sando
 */
@Component
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private WebSocketFrameToWsPacketCodec webSocketFrameToWsPacketCodec;
    @Autowired
    private WsPacketInboundHandler wsPacketInboundHandler;
    @Autowired
    private AbstractPacketDispacherHandler abstractPacketDispacherHandler;
    @Autowired
    private AbstractPacketOutboundHandler abstractPacketOutboundHandler;

    // 在 "ws://localhost:netPort"+WEBSOCKET_PATH 提供websocket服务
    private static final String WEBSOCKET_PATH = "/";

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // websocket协议本身是基于http协议的，所以这边也要使用http解编码器
        pipeline.addLast("Inbound:ByteBuf2HttpRequest和Outbound:HttpResponse2ByteBuf",new HttpServerCodec());
        // 向客户端发送HTML5文件，主要用于支持浏览器和服务器进行WebSocket通信
        pipeline.addLast("chunkedWriteHandler",new ChunkedWriteHandler());
        // 通常接收到的http是一个片段，如果想要完整接受一次请求所有数据，我们需要绑定HttpObjectAggregator
        // netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
        pipeline.addLast("httpObjectAggregator",new HttpObjectAggregator(65536));
        /**
         * 消除运行websocket服务器的粗活处理器
         * 它可以帮我们处理握手（handshaking）和控制帧（control frames (Close, Ping, Pong)），
         * 文本和二进制WebSocketFrame将会交给下一个你自己实现的handler处理
         */
        pipeline.addLast("Inbound:WebSocketFrame2WebSocketFrame",new WebSocketServerProtocolHandler(WEBSOCKET_PATH));
        // inbound:接收WebSocketServerProtocolHandler传来的WebSocketFrame，转化为WsPacket后继续往后传
        pipeline.addLast("Inbound:WebSocketFrame2WsPacket和Outbound:WsPacket2WebSocketFrame",webSocketFrameToWsPacketCodec);

        // 用sessionHandler sessionManager专门记录不同的用户
        // pipeline.addLast("sessionHandler",);

        pipeline.addLast("Inbound:WsPacket2AbstractPacket",wsPacketInboundHandler);
        pipeline.addLast("Inbound:AbstractPacket2End",abstractPacketDispacherHandler);
        pipeline.addLast("Outbound:AbstractPacket2WsPacket",abstractPacketOutboundHandler);
    }
}

