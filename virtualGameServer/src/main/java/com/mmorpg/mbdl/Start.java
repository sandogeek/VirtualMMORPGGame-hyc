package com.mmorpg.mbdl;

import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import com.mmorpg.mbdl.framework.communicate.websocket.model.PacketIdTsGenerator;
import com.mmorpg.mbdl.framework.communicate.websocket.model.ProtoGenerator;
import com.mmorpg.mbdl.framework.communicate.websocket.server.WebSocketServer;
import com.mmorpg.mbdl.framework.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {
    private static Logger logger= LoggerFactory.getLogger(Start.class);
    public static void  main(String[] args) throws Exception {
        clearProto(ProtoGenerator.PROTO_PATH);
        // 启动spring容器
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        PacketIdTsGenerator.generatePacketIdTs();
        removeAbstractPacketBean(ctx);
        logger.info("开始启动WebSocket服务器...");
        WebSocketServer webSocketServer = WebSocketServer.getInstance();
        webSocketServer.bind(WebSocketServer.PORT);
    }

    /**
     * 在Spring IOC启动后去除容器中AbstractPacket的单例
     * @param ctx spring上下文
     */
    static void removeAbstractPacketBean(ClassPathXmlApplicationContext ctx){
        for (String beanName :
                ctx.getBeanNamesForType(AbstractPacket.class)) {
            BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory)ctx.getBeanFactory();
            beanDefReg.removeBeanDefinition(beanName);
        }
    }
    /**
     * 清空dir下的.proto文件
     * @param dir
     */
    private static void clearProto(String dir) {
        FileUtils.clearByFileFilter(dir,true,FileUtils.extFileFilter("proto"));
    }
}
