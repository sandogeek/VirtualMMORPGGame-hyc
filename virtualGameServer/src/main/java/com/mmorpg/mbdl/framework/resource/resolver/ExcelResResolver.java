package com.mmorpg.mbdl.framework.resource.resolver;

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
    @Override
    public String getSuffix() {
        return ".xlsx";
    }

    @Override
    public void resolve() {
        // String fileName;
        // String pathToUse = StringUtils.cleanPath(SystemPropertyUtils.resolvePlaceholders(fileName));
        // if (pathToUse.startsWith("/")) {
        //     pathToUse = pathToUse.substring(1);
        // }
        Resource[] resources = null;
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            resources = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    "**/*" + getSuffix());
        } catch (IOException e) {
            String message = String.format("获取%s资源发生IO异常",getSuffix());
            logger.error(message);
            throw new RuntimeException(message);
        }
        if (resources!=null) {
            Arrays.stream(resources).filter(Resource::isReadable).forEach((res)->{
                String filename = res.getFilename();
            });
            // staticResDefinitionFactory.getFileNameSuffix2StaticResDefinitionTable().column()
        }
    }
}
