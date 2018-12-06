package com.mmorpg.mbdl;

import com.baidu.bjf.remoting.protobuf.ProtobufProxy;
import com.mmorpg.mbdl.framework.common.utils.FileUtils;
import com.mmorpg.mbdl.framework.common.utils.SpringPropertiesUtil;
import com.mmorpg.mbdl.framework.resource.core.StaticResHandler;
import com.mmorpg.mbdl.framework.thread.TaskExecutorGroup;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.logging.Level;

/**
 * 强化的启动器，其init方法会在{@link StaticResHandler#postProcessBeanFactory(ConfigurableListableBeanFactory)}中被调用
 * <p>在init方法中可以使用beanFactory获取bean，还可以使用{@link SpringPropertiesUtil#getProperty(String)}获取配置好的.properties文件的属性</p>
 *
 * @author Sando Geek
 * @since v1.0
 **/

public class EnhanceStarter {
    private static ConfigurableListableBeanFactory beanFactory;
    private static java.util.logging.Logger sysLogger = java.util.logging.Logger.getLogger(ProtobufProxy.class.getName());
    public static void init(){
        // 关闭jprotobuf的信息打印
        sysLogger.setLevel(Level.OFF);

        String PROTO_PATH = SpringPropertiesUtil.getProperty("dev.PROTO_PATH");
        clearProto(PROTO_PATH);
        // 初始化业务线程池
        TaskExecutorGroup.init();
    }

    public static void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        EnhanceStarter.beanFactory = beanFactory;
    }
    /**
     * 清空dir下的.proto文件
     * @param dir
     */
    private static void clearProto(String dir) {
        FileUtils.clearByFileFilter(dir,true,FileUtils.withSuffix(".proto"));
    }
}
