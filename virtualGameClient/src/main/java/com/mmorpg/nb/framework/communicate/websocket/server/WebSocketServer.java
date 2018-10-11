package com.mmorpg.nb.framework.communicate.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private static final int PORT = 8088;

    @Autowired
    private WebSocketServerInitializer webSocketServerInitializer;

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
                // .option(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(this.webSocketServerInitializer);
        // bootstrap.bind(netPort).sync();
        Channel channel = bootstrap.bind(netPort).sync().channel();
        // channel.closeFuture().sync();
        logger.info("WebSocket服务器已启动完成");
    }

    // 线程池关闭
    protected void shutdown() {
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }

    public static void  main(String[] args) throws Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        logger.info("开始启动WebSocket服务器...");
        WebSocketServer webSocketServer = applicationContext.getBean(WebSocketServer.class);
        webSocketServer.bind(PORT);
    }
}
