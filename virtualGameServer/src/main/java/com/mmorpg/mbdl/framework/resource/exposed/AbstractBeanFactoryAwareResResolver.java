package com.mmorpg.mbdl.framework.resource.exposed;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 包含beanFactory字段的静态资源解析器
 * 此时各种bean还没有实例化，所以这里不能使用@Autowired
 * @author Sando Geek
 * @since v1.0
 **/
public abstract class AbstractBeanFactoryAwareResResolver implements IResResolver {
    public static void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        AbstractBeanFactoryAwareResResolver.beanFactory = beanFactory;
    }

    protected static ConfigurableListableBeanFactory beanFactory;

}
