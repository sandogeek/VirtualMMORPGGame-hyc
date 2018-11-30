package com.mmorpg.mbdl;

import com.mmorpg.mbdl.framework.common.generator.PacketIdTsGenerator;
import com.mmorpg.mbdl.framework.common.utils.SpringPropertiesUtil;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import com.mmorpg.mbdl.framework.communicate.websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 启动spring容器,为了使用{@link SpringPropertiesUtil},部分启动逻辑放置在{@link EnhanceStarter#init()}中
 * <p>推荐main函数首行启动容器，容器初始化完毕前需要执行的逻辑写在{@link EnhanceStarter#init()}中</p>
 * @author Sando Geek
 */
public class Start {
    private static Logger logger= LoggerFactory.getLogger(Start.class);

    public static void  main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        // ConfigurableApplicationContext ctx = SpringApplication.run(Start.class, args);
        PacketIdTsGenerator.getInstance().generatePacketIdTs();
        removeAbstractPacketBean(ctx);
        logger.info("开始启动WebSocket服务器...");
        WebSocketServer webSocketServer = WebSocketServer.getInstance();
        webSocketServer.bind(WebSocketServer.PORT);
    }

    /**
     * 在Spring IOC启动后去除容器中AbstractPacket的单例
     * @param ctx spring上下文
     */
    static void removeAbstractPacketBean(ConfigurableApplicationContext ctx){
        for (String beanName :
                ctx.getBeanNamesForType(AbstractPacket.class)) {
            BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory)ctx.getBeanFactory();
            beanDefReg.removeBeanDefinition(beanName);
        }
    }
}
