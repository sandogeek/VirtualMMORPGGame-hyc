package com.mmorpg.mbdl.framework.resource.facade;

import org.springframework.beans.factory.BeanFactory;

/**
 * 包含beanFactory字段的静态资源解析器
 *
 * @author Sando Geek
 * @since v1.0
 **/
public abstract class AbstractBeanFactoryAwareResResolver implements IResResolver {
    public static void setBeanFactory(BeanFactory beanFactory) {
        AbstractBeanFactoryAwareResResolver.beanFactory = beanFactory;
    }

    protected static BeanFactory beanFactory;

}
