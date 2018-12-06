package com.mmorpg.mbdl.framework.common.utils;

import com.mmorpg.mbdl.EnhanceStarter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 使用硬编码的方式获取.properties中的属性
 * form https://stackoverflow.com/questions/1771166/access-properties-file-programmatically-with-spring/6817902#6817902
 * @author Sando Geek
 * @since v1.0
 **/
public class SpringPropertiesUtil extends PropertyPlaceholderConfigurer {

    private static Map<String, String> propertiesMap;
    // Default as in PropertyPlaceholderConfigurer
    private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;

    @Override
    public void setSystemPropertiesMode(int systemPropertiesMode) {
        super.setSystemPropertiesMode(systemPropertiesMode);
        springSystemPropertiesMode = systemPropertiesMode;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        super.processProperties(beanFactory, props);

        propertiesMap = new HashMap<>(64);
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String valueStr = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
            propertiesMap.put(keyStr, valueStr);
        }
    }

    /**
     * 此方法只可以用在{@link EnhanceStarter#init()}方法中，或者容器启动后运行的任何代码中
     * @param name .properties属性名
     * @return .properties属性名
     */
    public static String getProperty(String name) {
        return propertiesMap.get(name);
    }

}
