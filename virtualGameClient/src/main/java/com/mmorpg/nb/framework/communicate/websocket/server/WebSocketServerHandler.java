package com.mmorpg.nb.framework.communicate.websocket.server;

import com.mmorpg.nb.bussiness.command.manager.CommandManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.HOST;

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
        logger.debug("\n                                                    ---------channelRead进入成功-----------");
        if (msg instanceof FullHttpRequest) {
            logger.debug("处理http请求");
            HttpHeaders httpHeaders = ((FullHttpRequest) msg).headers();
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }else {
            logger.debug("无法识别的消息类型");
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req){
        /*// 处理解码失败的http，返回一个状态为400 Bad Request的http响应包
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
            return;
        }
        // 只处理get请求，否则返回403 FORBIDDEN
        if (req.method() != GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
            return;
        }*/
        // 处理握手
        /*WebSocketServerHandshakerFactory webSocketServerHandshakerFactory =
                new WebSocketServerHandshakerFactory(getWebSocketLocation(req), null, true);
        handshaker = webSocketServerHandshakerFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            ChannelFuture channelFuture = handshaker.handshake(ctx.channel(), req);
            // 握手成功之后,业务逻辑
            if (channelFuture.isSuccess()) {
                logger.info("握手成功...");

            }
        }*/
    }
    // 获取websocket服务的url
    private String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HOST) + WEBSOCKET_PATH;
        return "ws://" + location;
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // 浏览器访问时，将显示一个错误码
        if (res.status().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            res.headers().set(CONTENT_LENGTH, res.content().readableBytes());
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req)||res.status().code() != 200){
            f.addListener(ChannelFutureListener.CLOSE);
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
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        logger.info("收到" + channel.remoteAddress() + " 握手请求");
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
}
