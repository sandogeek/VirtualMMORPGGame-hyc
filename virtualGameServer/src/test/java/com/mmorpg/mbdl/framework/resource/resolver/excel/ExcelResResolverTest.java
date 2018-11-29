package com.mmorpg.mbdl.framework.resource.resolver.excel;

import com.mmorpg.mbdl.framework.resource.core.IStaticResUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

class ExcelResResolverTest {
    private static Logger logger = LoggerFactory.getLogger(ExcelResResolverTest.class);
    @Test
    void 测试获取xlsx资源() {
        Resource[] resources;
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            // ClassPathResource classPathResource = new ClassPathResource("**/*.xlsx");
            resources = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    "**/*" + ".xlsx");
            String classes = IStaticResUtil.getClassPathSuffixWith("\\classes");
            String path = ((FileSystemResource) resources[0]).getPath();
            String pathToUse = path.replace(classes, "");
            if (pathToUse.startsWith("/")) {
                pathToUse = pathToUse.substring(1);
            }
            logger.info("");
        } catch (IOException e) {
            String message = String.format("获取%s资源发生IO异常", "xlsx");
            logger.error(message);
            throw new RuntimeException(message);
        }
    }
}