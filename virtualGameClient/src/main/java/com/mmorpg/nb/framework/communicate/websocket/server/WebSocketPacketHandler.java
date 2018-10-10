package com.mmorpg.nb.framework.communicate.websocket.server;

import com.mmorpg.nb.bussiness.command.manager.CommandManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class WebSocketPacketHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger= LoggerFactory.getLogger(WebSocketPacketHandler.class);


    private WebSocketServerHandshaker handshaker;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive    ");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
    }

    @Autowired
    private CommandManager commandManager;

    private static WebSocketPacketHandler instance;
    public static WebSocketPacketHandler getInstance(){return instance;}

    @PostConstruct
    private void init(){
        WebSocketPacketHandler.instance=this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 字符串流协议包样例：10001|{json格式串}
        /*msg= StringUtils.trim(msg);
        if(StringUtils.isBlank(msg)){
            return;
        }
        String packetString=StringUtils.left(msg,StringUtils.indexOf(msg,"|"));
        String jsonString=StringUtils.right(msg,StringUtils.indexOf(msg,"|"));
        int packetID = Integer.valueOf(packetString);*/
        logger.info("channelRead0()----------");
        if (msg instanceof HttpRequest) {
            logger.warn("客户端发来了http请求");
            return;
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
        if(frame instanceof TextWebSocketFrame){
            String command = ((TextWebSocketFrame)frame).text();
            logger.info("收到命令："+command);
            // 执行命令
            commandManager.executeCommand(command);
            // TODO 客户端用哪种WebSocketFrame就用哪种WebSocketFrame发回去
            ctx.channel().writeAndFlush(new TextWebSocketFrame(String.format("命令[%s]执行成功",command)));
//            ctx.channel().writeAndFlush(new TextWebSocketFrame("123456"));
        }else if(frame instanceof BinaryWebSocketFrame){
            logger.info("收到二进制消息："+((BinaryWebSocketFrame)frame).content().readableBytes());
            // BinaryWebSocketFrame binaryWebSocketFrame=new BinaryWebSocketFrame(Unpooled.buffer().writeBytes("xxx".getBytes()));
            // ctx.channel().writeAndFlush(binaryWebSocketFrame);
        }else if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame);
            return;
        }else {
            logger.error("不支持的WebSocketFrame子类型");
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel未注册");
    }

}
