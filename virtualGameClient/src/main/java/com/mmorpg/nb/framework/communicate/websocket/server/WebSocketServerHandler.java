package com.mmorpg.nb.framework.communicate.websocket.server;

import com.mmorpg.nb.bussiness.command.manager.CommandManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
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
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
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
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 字符串流协议包样例：10001|{json格式串}
        /*msg= StringUtils.trim(msg);
        if(StringUtils.isBlank(msg)){
            return;
        }
        String packetString=StringUtils.left(msg,StringUtils.indexOf(msg,"|"));
        String jsonString=StringUtils.right(msg,StringUtils.indexOf(msg,"|"));
        int packetID = Integer.valueOf(packetString);*/
        if (msg instanceof FullHttpRequest) {
            logger.error("还有http请求？");
            logger.debug(""+(FullHttpRequest)msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }else {
            logger.debug("无法识别的消息类型");
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
        ctx.close();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info(String.format("与客户端[channel.id=%s]间的channel Active",ctx.channel().id()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info(String.format("与客户端[channel.id=%s]间的channel Inactive",ctx.channel().id()));
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception{
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete){
            logger.info("握手成功");
        }

    }
}
