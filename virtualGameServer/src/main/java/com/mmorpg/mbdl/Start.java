package com.mmorpg.mbdl;

import com.mmorpg.mbdl.framework.communicate.websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {
    private static Logger logger= LoggerFactory.getLogger(Start.class);
    public static void  main(String[] args) throws Exception {
        // 启动spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        logger.info("开始启动WebSocket服务器...");
        WebSocketServer webSocketServer = WebSocketServer.getInstance();
        webSocketServer.bind(WebSocketServer.PORT);
    }
}
