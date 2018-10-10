package com.mmorpg.nb.framework.communicate.socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于socket的客户端
 * @author sando
 */
public class SocketClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private static final String IP = "127.0.0.1";
    private static final int PORT = 8088;

    private static final EventLoopGroup GROUP = new NioEventLoopGroup();

    @SuppressWarnings("rawtypes")
    protected static void  run() throws Exception {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(GROUP);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) {
                ChannelPipeline channelPipeline = channel.pipeline();
                channelPipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                channelPipeline.addLast("decoder", new StringDecoder());
                channelPipeline.addLast("encoder", new StringEncoder());
                channelPipeline.addLast(new SocketClientHandler());

            }
        });

        // 连接服务端
        Channel channel = bootstrap.connect(IP,PORT).sync().channel();

        String message="客户端数据c";
        channel.writeAndFlush(message);
        Thread.sleep(5000);
        channel.writeAndFlush("怎么说");
        channel.writeAndFlush("\r\n");
        channel.writeAndFlush("怎么说");


        logger.info("向Socket服务器发送数据:"+message);
    }

    public static void  main(String[] args){
        logger.info("开始连接Socket服务器...");
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            GROUP.shutdownGracefully();
        }
    }

}
