package com.mmorpg.mbdl.framework.resource.exposed;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.type.classreading.MetadataReader;

/**
 * 所有.class文件的MetadataReader的后处理器
 *
 * @author Sando Geek
 * @since v1.0 2018/12/6
 **/
@edu.umd.cs.findbugs.annotations.SuppressFBWarnings("MS_CANNOT_BE_FINAL")
public abstract class AbstractMetadataReaderPostProcessor {
    protected static ConfigurableListableBeanFactory beanFactory;
    /**
     * 所有的.class文件的MetadataReader
     * @param metadataReader
     * @return 感兴趣的类的集合
     */
    public abstract void postProcessMetadataReader(MetadataReader metadataReader);

    public static void setBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        AbstractMetadataReaderPostProcessor.beanFactory = beanFactory;
    }
}
