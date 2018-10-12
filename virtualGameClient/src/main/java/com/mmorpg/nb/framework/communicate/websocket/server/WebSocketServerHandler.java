package com.mmorpg.nb.framework.communicate.websocket.server;

import com.mmorpg.nb.bussiness.command.manager.CommandManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sando
 */
@ChannelHandler.Sharable
@Component
public class WebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private static final Logger logger= LoggerFactory.getLogger(WebSocketServerHandler.class);
    // 在 "ws://localhost:8088"+WEBSOCKET_PATH 提供websocket服务
    private static final String WEBSOCKET_PATH = "";

    private WebSocketServerHandshaker handshaker;
    @Autowired
    private CommandManager commandManager;

    private static WebSocketServerHandler instance;
    public static WebSocketServerHandler getInstance(){return instance;}

    @PostConstruct
    private void init(){
        WebSocketServerHandler.instance=this;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // 字符串流协议包样例：10001|{json格式串}
        /*msg= StringUtils.trim(msg);
        if(StringUtils.isBlank(msg)){
            return;
        }
        String packetString=StringUtils.left(msg,StringUtils.indexOf(msg,"|"));
        String jsonString=StringUtils.right(msg,StringUtils.indexOf(msg,"|"));
        int packetID = Integer.valueOf(packetString);*/
        if(frame instanceof TextWebSocketFrame){
            String command = ((TextWebSocketFrame)frame).text();
            logger.info("收到TextWebSocketFrame，内容为："+command);
            // 执行命令
            commandManager.executeCommand(command);
            // TODO 客户端用哪种WebSocketFrame就用哪种WebSocketFrame发回去
            ctx.channel().writeAndFlush(new TextWebSocketFrame(String.format("命令[%s]执行成功",command)));
//            ctx.channel().writeAndFlush(new TextWebSocketFrame("123456"));
        }else if(frame instanceof BinaryWebSocketFrame){
            logger.info("收到二进制消息："+((BinaryWebSocketFrame)frame).content().readableBytes());
            // BinaryWebSocketFrame binaryWebSocketFrame=new BinaryWebSocketFrame(Unpooled.buffer().writeBytes("xxx".getBytes()));
            // ctx.channel().writeAndFlush(binaryWebSocketFrame);
        }else {
            logger.error("不支持的WebSocketFrame子类型");
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("捕获到异常："+cause);
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
            logger.info("握手成功");
        }

    }
}
