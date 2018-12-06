package com.mmorpg.mbdl.framework.resource.resolver.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.mmorpg.mbdl.framework.resource.core.StaticResDefinition;
import com.mmorpg.mbdl.framework.resource.exposed.AbstractBeanFactoryAwareResResolver;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 表格型json资源解析器
 *
 * @author Sando Geek
 * @since v1.0 2018/12/6
 **/
@Component
public class JsonResResolver extends AbstractBeanFactoryAwareResResolver {
    private static Logger logger = LoggerFactory.getLogger(JsonResResolver.class);
    private static ObjectMapper mapper;
    static  {
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }
    @Override
    public String suffix() {
        return ".json";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void resolve(StaticResDefinition staticResDefinition) {
        ImmutableMap.Builder key2ResourceBuilder = ImmutableMap.builder();
        try (InputStream inputStream = staticResDefinition.getResource().getInputStream()){
            List vClassObjects = mapper.readValue(inputStream, mapper.getTypeFactory().constructCollectionType(List.class, staticResDefinition.getvClass()));
            vClassObjects.forEach(o -> {
                try {
                    key2ResourceBuilder.put(staticResDefinition.getIdField().get(o),o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            ImmutableMap key2Resource = key2ResourceBuilder.build();
            staticResDefinition.getStaticRes().setKey2Resource(key2Resource);
            String resBeanName = StringUtils.uncapitalize(staticResDefinition.getiStaticRes().getClass().getSimpleName());
            beanFactory.registerSingleton(resBeanName,staticResDefinition.getiStaticRes());
        } catch (IOException e) {
            throw new RuntimeException(String.format("读取资源文件[%s]发生异常",staticResDefinition.getFullFileName()),e);
        }
    }
}
