package com.mmorpg.nb.framework.communicate.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
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
        try {
            // bossGroup 用来处理连接
            this.bossGroup = new NioEventLoopGroup(THREADSIZEGROUPFORBUSSINESS);
            // wokerGroup用来处理后续事件
            this.workerGroup = new NioEventLoopGroup(THREAD_SIZE_EACH_GROUP);
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class)
                    /**
                     * 禁用nagle算法
                     * Nagle算法试图减少TCP包的数量和结构性开销, 将多个较小的包组合成较大的包进行发送.但这不是重点,
                     * 关键是这个算法受TCP延迟确认影响, 会导致相继两次向连接发送请求包,
                     * 读数据时会有一个最多达500毫秒的延时.
                     * TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，同时，对方接收到数据，也需要发送ACK表示确认。
                     * 为了尽可能的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。
                     * （一个连接会设置MSS参数，因此，TCP/IP希望每次都能够以MSS尺寸的数据块来发送数据）。
                     * Nagle算法就是为了尽可能发送大块数据，避免网络中充斥着许多小数据块。
                     */
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    /**
                     * 当设置为true的时候，TCP会实现监控连接是否有效，当连接处于空闲状态的时候，超过了2个小时，
                     * 本地的TCP实现会发送一个数据包给远程的 socket，如果远程没有发回响应，TCP会持续尝试11分钟，知道响应为止，
                     * 如果在12分钟的时候还没响应，TCP尝试关闭socket连接。
                     * TODO 时间太长，应自己实现保活机制（通常是心跳机制）
                     */
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(this.webSocketServerInitializer);
            Channel channel = bootstrap.bind(netPort).sync().channel();
            logger.info("WebSocket服务器已启动完成");
            // channel.closeFuture().sync();
        }  catch (Exception e){
            shutdown();
            throw e;
        }
    }

    // 线程池关闭
    public void shutdown() {
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
