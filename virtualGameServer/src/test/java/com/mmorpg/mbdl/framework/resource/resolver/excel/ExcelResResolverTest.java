package com.mmorpg.mbdl.framework.resource.resolver.excel;

import com.mmorpg.mbdl.framework.resource.core.IStaticResUtil;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.stream.IntStream;

class ExcelResResolverTest {
    private static Logger logger = LoggerFactory.getLogger(ExcelResResolverTest.class);
    @Test
    void 测试获取xlsx资源() {
        Resource[] resources;
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            resources = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    "**/*" + ".xlsx");
            String relative2ClassPath = IStaticResUtil.getResPathRelative2ClassPath((FileSystemResource) resources[0]);
            logger.info("");
        } catch (IOException e) {
            String message = String.format("获取%s资源发生IO异常", "xlsx");
            logger.error(message);
            throw new RuntimeException(message);
        }
    }

    @Test
    void 并行流测试() {
        Runnable r = () -> IntStream
                .range(-10000_0000, +10000_0000)
                .parallel()
                .map(i -> {
                    return Thread.activeCount();
                })
                .max()
                .ifPresent(value -> logger.info("活跃线程最大数量{}",value));
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ForkJoinPool.commonPool().submit(r).join();
        stopWatch.stop();
        logger.info("默认ForkJoinPool耗时{}ms",stopWatch.getTime());
        stopWatch.reset();
        final ForkJoinPool.ForkJoinWorkerThreadFactory factory = pool -> {
            final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            worker.setName("my-thread-prefix-name-" + worker.getPoolIndex());
            return worker;
        };
        stopWatch.start();
        new ForkJoinPool(16,factory,null,false).submit(r).join();
        stopWatch.stop();
        logger.info("自定义ForkJoinPool耗时{}ms",stopWatch.getTime());

    }
}