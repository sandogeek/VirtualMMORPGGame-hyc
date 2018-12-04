package com.mmorpg.mbdl.framework.resource.resolver;

import com.mmorpg.mbdl.framework.resource.exposed.IResResolver;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * 静态资源解析器工厂
 *
 * @author Sando Geek
 * @since v1.0
 **/
public class ResResolverFactory implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    /**
     * suffix -> ResResolver
     */
    private Map<String,IResResolver> suffix2ResResolver;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    @PostConstruct
    private void init(){
        applicationContext.getBeansOfType(IResResolver.class).forEach((k,v)->{
            suffix2ResResolver.put(v.suffix(),v);
        });
    }
}
