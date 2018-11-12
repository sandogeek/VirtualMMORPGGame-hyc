package com.mmorpg.mbdl.framework.common.generator;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.ResolvableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;

/**
 * JpaRepository泛型子接口生成器
 * @author sando
 */
@Component
public class JpaRepositoryGenerator implements BeanFactoryPostProcessor {
    // private static final HashMap
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    /**
     * 获取用户自定义的JpaRepository泛型父接口,用于确定是否需要生成JpaRepository接口
     * @return
     */
    private HashMap<Object,ResolvableType> getUserDifineJpaRepoMap(){
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.mmorpg.mbdl.bussiness"))
                .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
                .filterInputsBy(new FilterBuilder().include(".*\\.dao.*")));
        Set<Class<? extends JpaRepository>> jpaRepoInterfaceSet = reflections.getSubTypesOf(JpaRepository.class);
        HashMap<ResolvableType,Class<? extends JpaRepository>> resolvableTypeClassHashMap = new HashMap<>(50);
        // 初始化resolvableTypeClassHashMap
        for (Class<? extends JpaRepository> clazz :
                jpaRepoInterfaceSet) {
            for (ResolvableType resolvableType :
                    ResolvableType.forClass(clazz).getInterfaces()) {
                resolvableTypeClassHashMap.put(resolvableType,clazz);
            }
        }
        HashMap<Object,ResolvableType> objectResolvableTypeHashMap = new HashMap<>(40);
        for (ResolvableType resolvableType:
                resolvableTypeClassHashMap.keySet()) {
            if (objectResolvableTypeHashMap.keySet().contains(resolvableType.getSource())){
                Class<? extends JpaRepository> oldClass = resolvableTypeClassHashMap.get(objectResolvableTypeHashMap.get(resolvableType.getSource()));
                Class<? extends JpaRepository> newClass = resolvableTypeClassHashMap.get(resolvableType);
                throw new RuntimeException(String.format("dao类[%s]与类[%s]有相同的泛型父类接口%s",oldClass.getSimpleName(),newClass.getSimpleName(),resolvableType.toString()));
            }
            objectResolvableTypeHashMap.put(resolvableType.getSource(),resolvableType);
        }
        return  objectResolvableTypeHashMap;
    }
}
