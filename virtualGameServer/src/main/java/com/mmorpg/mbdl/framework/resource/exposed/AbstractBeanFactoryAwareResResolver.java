package com.mmorpg.mbdl.framework.resource.exposed;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * 包含beanFactory字段的静态资源解析器
 * 此时各种bean还没有实例化，所以这里不能使用@Autowired
 * @author Sando Geek
 * @since v1.0
 **/
@SuppressFBWarnings("MS_CANNOT_BE_FINAL")
public abstract class AbstractBeanFactoryAwareResResolver implements IResResolver {
    public static void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        AbstractBeanFactoryAwareResResolver.beanFactory = beanFactory;
    }

    protected static ConfigurableListableBeanFactory beanFactory;

}
