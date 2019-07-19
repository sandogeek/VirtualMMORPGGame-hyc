package com.mmorpg.mbdl.framework.resource.impl;

import com.mmorpg.mbdl.framework.resource.annotation.ResDef;
import com.mmorpg.mbdl.framework.resource.core.StaticResDefinition;
import com.mmorpg.mbdl.framework.resource.core.StaticResHandler;
import com.mmorpg.mbdl.framework.resource.exposed.AbstractMetadataReaderPostProcessor;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

/**
 * 带@Resdef注解的类的MetadataReader后处理器
 *
 * @author Sando Geek
 * @since v1.0 2018/12/6
 **/
@Component
public class ResdefMetadataReaderPostProcessor extends AbstractMetadataReaderPostProcessor {
    @Override
    public void postProcessMetadataReader(MetadataReader metadataReader) {
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        if (!annotationMetadata.hasAnnotation(ResDef.class.getName())) {
            return;
        }
        try {
            Class<?> resClazz = Class.forName(metadataReader.getClassMetadata().getClassName());
            StaticResHandler staticResHandler = beanFactory.getBean(StaticResHandler.class);
            StaticResDefinition staticResDefinition = new StaticResDefinition();
            staticResDefinition.setBeanFactory(beanFactory);
            staticResHandler.getClass2StaticResDefinitionMap().put(resClazz, staticResDefinition);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("无法加载类[%s]",metadataReader.getClassMetadata().getClassName()));
        }

    }
}
