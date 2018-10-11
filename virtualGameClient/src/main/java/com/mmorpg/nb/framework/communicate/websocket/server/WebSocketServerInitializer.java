package com.mmorpg.nb.framework.communicate.websocket.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
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
    private WebSocketPacketHandler webSocketPacketHandler;

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // websocket协议本身是基于http协议的，所以这边也要使用http解编码器
        pipeline.addLast("httpCodec",new HttpServerCodec());
        // 以块的方式来写的处理器
        pipeline.addLast("chunkedWriteHandler",new ChunkedWriteHandler());
        // netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
        pipeline.addLast("httpObjectAggregator",new HttpObjectAggregator(1024*1024*1024));
        // pipeline.addLast(new WebSocketServerCompressionHandler());
        // ws://server:port/websocketPath
        // 于是客户端连接此websocketServer需要访问：ws://localhost:8088/
        // pipeline.addLast(new WebSocketServerProtocolHandler("",null,true,65535));

        // 目前每个浏览器端只连接一个本应用，所以这里不需要区分来自哪个浏览器，不需要像真正的服务端那样
        // 用sessionHandler sessionManager专门记录不同的用户
        // pipeline.addLast("sessionHandler",);

        // websocket定义了传递数据的6中frame类型
        pipeline.addLast(webSocketPacketHandler);
    }
}
