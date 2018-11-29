package com.mmorpg.mbdl.framework.resource.resolver.excel;

import com.mmorpg.mbdl.framework.resource.core.StaticResDefinition;
import com.mmorpg.mbdl.framework.resource.core.StaticResDefinitionFactory;
import com.mmorpg.mbdl.framework.resource.facade.AbstractBeanFactoryAwareResResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * Excel静态资源解析器
 *
 * @author Sando Geek
 * @since v1.0
 **/
@Component
public class ExcelResResolver extends AbstractBeanFactoryAwareResResolver {
    private static final Logger logger = LoggerFactory.getLogger(ExcelResResolver.class);
    @Autowired
    private StaticResDefinitionFactory staticResDefinitionFactory;
    @Autowired
    private IExcelFormat iExcelFormat;
    @Override
    public String suffix() {
        return ".xlsx";
    }

    @Override
    public void resolve() {
        // String fileName;
        // String pathToUse = StringUtils.cleanPath(SystemPropertyUtils.resolvePlaceholders(fileName));
        // if (pathToUse.startsWith("/")) {
        //     pathToUse = pathToUse.substring(1);
        // }
        // 需要一个 ArrayList下标到字段下标的映射
        Resource[] resources;
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            resources = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    "**/*" + suffix());
        } catch (IOException e) {
            String message = String.format("获取%s资源发生IO异常", suffix());
            logger.error(message);
            throw new RuntimeException(message);
        }

        Map<String, StaticResDefinition> fileName2StaticResDefinition = staticResDefinitionFactory.getFullFileNameStaticResDefinition();
        Optional.ofNullable(resources).ifPresent(notNullResources -> {
            Arrays.stream(notNullResources).filter(Resource::isReadable).forEach((res)->{
                String filename = res.getDescription();
            });
        });
    }
}
