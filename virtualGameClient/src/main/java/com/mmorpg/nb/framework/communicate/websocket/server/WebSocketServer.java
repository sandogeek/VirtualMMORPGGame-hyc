package com.mmorpg.nb.framework.communicate.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private static final int PORT = 8088;

    @Autowired
    private WebSocketPacketHandler webSocketPacketHandler;

    protected static final int THREADSIZEGROUPFORBUSSINESS = Runtime.getRuntime().availableProcessors()*2;
    protected static final int THREAD_SIZE_EACH_GROUP = 4;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    protected void  bind(int netPort) throws Exception {
        // bossGroup 用来处理连接
        this.bossGroup = new NioEventLoopGroup(THREADSIZEGROUPFORBUSSINESS);
        // wokerGroup用来处理后续事件
        this.workerGroup = new NioEventLoopGroup(THREAD_SIZE_EACH_GROUP);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                // websocket协议本身是基于http协议的，所以这边也要使用http解编码器
                pipeline.addLast("httpCodec",new HttpServerCodec());
                // 以块的方式来写的处理器
                pipeline.addLast("chunkedWriteHandler",new ChunkedWriteHandler());
                // netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
                pipeline.addLast("httpObjectAggregator",new HttpObjectAggregator(1024*1024*1024));
                // ws://server:port/websocketPath
                // 于是客户端连接此websocketServer需要访问：ws://localhost:8088/
                pipeline.addLast(new WebSocketServerProtocolHandler("",null,true,65535));

                // 目前每个浏览器端只连接一个本应用，所以这里不需要区分来自哪个浏览器，不需要像真正的服务端那样
                // 用sessionHandler sessionManager专门记录不同的用户
                // pipeline.addLast("sessionHandler",);

                // websocket定义了传递数据的6中frame类型
                pipeline.addLast(webSocketPacketHandler);
            }
        });
        bootstrap.bind(netPort).sync();
        // Channel channel = bootstrap.bind(netPort).sync().channel();
        // channel.closeFuture().sync();
        logger.info("WebSocket服务器已启动完成");
    }

    // 线程池关闭
    protected void shutdown() {
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }

    public static void  main(String[] args) throws Exception {
        logger.info("开始启动WebSocket服务器...");
        new WebSocketServer().bind(PORT);
    }
}
