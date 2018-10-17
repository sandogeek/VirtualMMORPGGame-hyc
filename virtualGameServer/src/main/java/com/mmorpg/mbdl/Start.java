package com.mmorpg.mbdl;

import com.mmorpg.mbdl.bussiness.common.AbstractPacket;
import com.mmorpg.mbdl.framework.communicate.websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {
    private static Logger logger= LoggerFactory.getLogger(Start.class);
    public static void  main(String[] args) throws Exception {
        // 启动spring容器
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        removeAbstractPacketBean(ctx);
        logger.info("开始启动WebSocket服务器...");
        WebSocketServer webSocketServer = WebSocketServer.getInstance();
        webSocketServer.bind(WebSocketServer.PORT);
    }

    /**
     * 在Spring IOC启动后去除容器中AbstractPacket的单例
     * @param ctx
     */
    static void removeAbstractPacketBean(ClassPathXmlApplicationContext ctx){
        for (String beanName :
                ctx.getBeanNamesForType(AbstractPacket.class)) {
            BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory)ctx.getBeanFactory();
            beanDefReg.removeBeanDefinition(beanName);
        }
    }
}
