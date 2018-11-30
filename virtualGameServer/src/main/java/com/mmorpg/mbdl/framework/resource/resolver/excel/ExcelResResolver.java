package com.mmorpg.mbdl.framework.resource.resolver.excel;

import com.mmorpg.mbdl.framework.common.utils.SpringPropertiesUtil;
import com.mmorpg.mbdl.framework.resource.core.IStaticResUtil;
import com.mmorpg.mbdl.framework.resource.core.StaticResDefinition;
import com.mmorpg.mbdl.framework.resource.core.StaticResDefinitionFactory;
import com.mmorpg.mbdl.framework.resource.facade.AbstractBeanFactoryAwareResResolver;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * Excel静态资源解析器
 * 注意：此时各种bean还没有实例化，所以这里不能使用@Autowired
 * @author Sando Geek
 * @since v1.0
 **/
@Component
public class ExcelResResolver extends AbstractBeanFactoryAwareResResolver {
    private static final Logger logger = LoggerFactory.getLogger(ExcelResResolver.class);

    @Override
    public String suffix() {
        return ".xlsx";
    }

    @Override
    public void resolve() {
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
        Map<String, StaticResDefinition> fileName2StaticResDefinition = beanFactory
                .getBean(StaticResDefinitionFactory.class).getFullFileNameStaticResDefinition();
        // 利用ForkJoinPool并行处理，因为包含IO,所以使用
        Runnable resourceLoadTask = () -> {
            Arrays.stream(resources).parallel().filter(Resource::isReadable).map((res)->{
                String filename = res.getFilename();
                // 初始化StaticResDefinition的List<Resource> resources字段
                StaticResDefinition staticResDefinitionResult = Optional.ofNullable(fileName2StaticResDefinition.get(filename)).orElseGet(() -> {
                    String resPathRelative2ClassPath = IStaticResUtil.getResPathRelative2ClassPath((FileSystemResource) res);
                    return fileName2StaticResDefinition.get(resPathRelative2ClassPath);
                });
                if (staticResDefinitionResult!=null){
                    Resource resource = staticResDefinitionResult.getResource();
                    if ( resource != null ){
                        String newPath = IStaticResUtil.getResPathRelative2ClassPath((FileSystemResource) res);
                        String oldPath = IStaticResUtil.getResPathRelative2ClassPath((FileSystemResource) resource);
                        String message = String.format(
                                "资源类[%s]对应两份文件：[%s],[%s],请在其注解上使用@ResDef(relativePath = \"%s\")或@ResDef(relativePath = \"%s\")确定此类对应的资源文件",
                                staticResDefinitionResult.getvClass().getSimpleName(),
                                newPath, oldPath, newPath, oldPath
                        );
                        throw new RuntimeException(message);
                    }
                    staticResDefinitionResult.setResource(res);
                }
                return staticResDefinitionResult;
            }).filter(Objects::nonNull).forEach((staticResDefinition -> {
                logger.debug("静态资源{}成功关联类{}",staticResDefinition.getFullFileName(),staticResDefinition.getvClass().getSimpleName());
            }));
        };
        StopWatch stopWatch = new StopWatch();

        // 使用默认ForkJoinPool执行耗时测试
        // stopWatch.start();
        // resourceLoadTask.run();
        // stopWatch.stop();
        // logger.info("默认的ForkJoinPool解析静态资源耗时{}ms",stopWatch.getTime());
        // stopWatch.reset();

        final ForkJoinPool.ForkJoinWorkerThreadFactory factory = pool -> {
            final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            worker.setName("资源加载线程-" + worker.getPoolIndex());
            return worker;
        };
        stopWatch.start();
        String threadSize = SpringPropertiesUtil.getProperty("sever.config.static.res.load.thread.size");
        new ForkJoinPool(Integer.parseInt(threadSize),factory,null,false).submit(resourceLoadTask).join();
        stopWatch.stop();
        logger.info("静态资源解析完毕{}ms",stopWatch.getTime());
        logger.info("");
    }

    // private StaticResDefinition getFromMapByRelative2ClassPath(Map<String, StaticResDefinition> fileName2StaticResDefinition,String resPathRelative2ClassPath){
    //     String[] splitPath = resPathRelative2ClassPath.split("/");
    //     String pathToUse = splitPath[splitPath.length-1];
    //     for (int i = splitPath.length -2; i > -1; i--) {
    //         logger.info(pathToUse);
    //         pathToUse = splitPath[i] + "/" + pathToUse;
    //     }
    //     logger.info(pathToUse);
    //     return new StaticResDefinition();
    // }
}
