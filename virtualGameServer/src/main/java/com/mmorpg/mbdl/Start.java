package com.mmorpg.mbdl;

import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.mmorpg.mbdl.framework.common.generator.PacketIdTsGenerator;
import com.mmorpg.mbdl.framework.common.utils.FileUtils;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import com.mmorpg.mbdl.framework.communicate.websocket.server.WebSocketServer;
import com.mmorpg.mbdl.framework.thread.TaskExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;
import java.util.logging.Level;

// @SpringBootApplication
public class Start {
    private static Logger logger= LoggerFactory.getLogger(Start.class);

    public static void  main(String[] args) throws Exception {
        // 关闭jprotobuf的信息打印
        java.util.logging.Logger sysLogger = java.util.logging.Logger.getLogger(ProtobufProxy.class.getName());
        sysLogger.setLevel(Level.WARNING);

        Resource resource = new ClassPathResource("/dev.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        clearProto(props.getProperty("dev.PROTO_PATH"));
        // 初始化业务线程池
        TaskExecutorGroup.init();
        // 启动spring容器
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
    /**
     * 清空dir下的.proto文件
     * @param dir
     */
    private static void clearProto(String dir) {
        FileUtils.clearByFileFilter(dir,true,FileUtils.extFileFilter("proto"));
    }
}
