package com.mmorpg.mbdl.framework.resource.core;

import com.google.common.base.Preconditions;
import com.mmorpg.mbdl.EnhanceStarter;
import com.mmorpg.mbdl.framework.common.utils.SpringPropertiesUtil;
import com.mmorpg.mbdl.framework.resource.annotation.Id;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;
import com.mmorpg.mbdl.framework.resource.exposed.AbstractBeanFactoryAwareResResolver;
import com.mmorpg.mbdl.framework.resource.exposed.AbstractMetadataReaderPostProcessor;
import com.mmorpg.mbdl.framework.resource.exposed.IResResolver;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import com.mmorpg.mbdl.framework.resource.impl.StaticRes;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;

/**
 * 静态资源处理器
 * @author Sando Geek
 * @since v1.0
 **/
@SuppressFBWarnings("NP_NONNULL_PARAM_VIOLATION")
@Component
public class StaticResHandler implements BeanFactoryPostProcessor {
    private static Logger logger = LoggerFactory.getLogger(StaticResHandler.class);
    private String packageToScan;
    private String suffix;
    private Map<Class, StaticResDefinition> class2StaticResDefinitionMap = new HashMap<>(32);
    /**
     * 自定义的用于IO的ForkJoinPool
     */
    ForkJoinPool forkJoinPool;
    {
        final ForkJoinPool.ForkJoinWorkerThreadFactory factory = pool -> {
            final ForkJoinWorkerThread worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            worker.setName("资源加载线程-" + worker.getPoolIndex());
            return worker;
        };

        String threadSize = SpringPropertiesUtil.getProperty("sever.config.static.res.load.thread.size");
        forkJoinPool = new ForkJoinPool(Integer.parseInt(threadSize), factory, null, false);
    }

    public Map<Class, StaticResDefinition> getClass2StaticResDefinitionMap() {
        return class2StaticResDefinitionMap;
    }

    // public void setPackageToScan(String packageToScan) {
    //     Preconditions.checkArgument(packageToScan!=null,"静态资源未配置包扫描路径");
    //     this.packageToScan = packageToScan;
    // }

    // public void setSuffix(String suffix) {
    //     Preconditions.checkArgument(suffix!=null,"静态资源未配置默认静态资源后缀");
    //     this.suffix = suffix;
    // }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        AbstractBeanFactoryAwareResResolver.setBeanFactory(beanFactory);
        /** 使SpringPropertiesUtil可用于{@link EnhanceStarter}中 */
        beanFactory.getBean(SpringPropertiesUtil.class);
        EnhanceStarter.setBeanFactory(beanFactory);
        AbstractMetadataReaderPostProcessor.setBeanFactory(beanFactory);
        EnhanceStarter.init();

        String suffix = SpringPropertiesUtil.getProperty("sever.config.static.res.load.suffix");
        this.suffix = StringUtils.isEmpty(suffix)?".xlsx":suffix;
        this.packageToScan = SpringPropertiesUtil.getProperty("sever.config.static.res.load.package.scan");
        Preconditions.checkNotNull(packageToScan,"静态资源包扫描路径不能为空");

        classesScan(packageToScan,beanFactory);
        init(class2StaticResDefinitionMap,beanFactory);
        handleStaticRes(beanFactory);
    }

    /**
     * 处理静态资源
     * @param beanFactory bean工厂
     */
    private void handleStaticRes(ConfigurableListableBeanFactory beanFactory){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        beanFactory.getBeansOfType(IResResolver.class).values().forEach(iResResolver -> {
            Resource[] resources;
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            try {
                resources = resourcePatternResolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                        "**/*" + iResResolver.suffix());
            } catch (IOException e) {
                String message = String.format("获取后缀为[%s]的资源时发生IO异常", iResResolver.suffix());
                logger.error(message);
                throw new RuntimeException(message);
            }
            Map<String, StaticResDefinition> fileName2StaticResDefinition = beanFactory
                    .getBean(StaticResDefinitionFactory.class).getFullFileNameStaticResDefinition();
            // 利用ForkJoinPool并行处理，因为包含IO,所以使用自定义的ForkJoinPool
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
                    logger.debug("静态资源{}成功关联到类[{}]",staticResDefinition.getFullFileName(),staticResDefinition.getvClass().getSimpleName());
                    iResResolver.resolve(staticResDefinition);
                }));
            };

            // 使用默认ForkJoinPool执行耗时测试
            // resourceLoadTask.run();

            try {
                forkJoinPool.submit(resourceLoadTask).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("静态资源解析失败",e);
            }

        });
        stopWatch.stop();
        forkJoinPool.shutdown();
        checkNullResStaticResDefinition(beanFactory
                .getBean(StaticResDefinitionFactory.class).getFullFileNameStaticResDefinition());
        logger.info("静态资源解析完毕，耗时{}ms",stopWatch.getTime());
    }

    /**
     * 检查是否存在Resource字段为null的StaticResDefinition
     */
    private void checkNullResStaticResDefinition(Map<String, StaticResDefinition> fileName2StaticResDefinition) {
        fileName2StaticResDefinition.values().stream()
                .filter(staticResDefinition -> staticResDefinition.getResource()==null).findAny()
        .ifPresent(staticResDefinition -> {
            throw new RuntimeException(
                    String.format("静态资源类[%s]找不到对应的资源文件[%s]",staticResDefinition.getvClass().getSimpleName(),staticResDefinition.getFullFileName()));
        });
    }

    /**
     * 初始化
     *
     * <p>初始化StaticResDefinitionFactory,每个表格型资源类生成一个对应的IStaticRes的子类,并存放到StaticResDefinition</p>
     * @param class2StaticResDefinitionMap
     * @return 资源类clazz -> 对应的baseClass的子类,如果集合大小为0，返回null
     */
    @SuppressWarnings("unchecked")
    private void init(Map<Class,StaticResDefinition> class2StaticResDefinitionMap, ConfigurableListableBeanFactory beanFactory){
        if (class2StaticResDefinitionMap.size() == 0) {
            return;
        }
        class2StaticResDefinitionMap.keySet().forEach((clazz)->{
            ResDef resDef = (ResDef) clazz.getAnnotation(ResDef.class);
            StaticResDefinition staticResDefinition = class2StaticResDefinitionMap.get(clazz);

            /** 表格型资源，检查其Id的唯一性，并生成{@link IStaticRes}的子类实例 */
            if (resDef.isTable()){
                Set<Field> fields = getAllFields(clazz, withAnnotation(Id.class));
                if (fields.size() > 1){
                    throw new RuntimeException(String.format("表格型资源类[%s]包含多个@Id注解的字段",clazz.getSimpleName()));
                }else if (fields.size() < 1){
                    throw new RuntimeException(String.format("表格型资源类[%s]不包含@Id注解的字段，如非表格型资源，请在其@ResDef中把isTable设置为false",clazz.getSimpleName()));
                }
                Field idField = fields.toArray(new Field[0])[0];
                staticResDefinition.setIdField(idField);
                Class idBoxedType = null;
                if (idField.getType().isPrimitive()){
                    switch (idField.getType().getSimpleName()) {
                        case ("int") : {
                            idBoxedType = Integer.class;
                            break;
                        }
                        case ("boolean") : {
                            idBoxedType = Boolean.class;
                            break;
                        }
                        case ("short") : {
                            idBoxedType = Short.class;
                            break;
                        }
                        case ("byte") : {
                            idBoxedType = Byte.class;
                            break;
                        }
                        case ("char") : {
                            idBoxedType = Character.class;
                            break;
                        }
                        case ("long") : {
                            idBoxedType = Long.class;
                            break;
                        }
                        case ("float") : {
                            idBoxedType = Float.class;
                            break;
                        }
                        case ("double") : {
                            idBoxedType = Double.class;
                            break;
                        }
                        default:
                    }
                }
                if (idBoxedType == null){
                    idBoxedType = idField.getType();
                }

                TypeDescription.Generic genericStaticRes =
                        TypeDescription.Generic.Builder.parameterizedType(StaticRes.class, idBoxedType, clazz).build();
                String packageName = clazz.getPackage().getName();
                Class<?> staticResSubClass = new ByteBuddy().subclass(genericStaticRes)
                        .name(packageName + "." + StaticRes.class.getSimpleName() + idBoxedType.getSimpleName() + clazz.getSimpleName())
                        .make().load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION).getLoaded();
                StaticRes staticRes = null;
                try {
                    staticRes = (StaticRes) staticResSubClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw  new RuntimeException(String.format("生成%s的子类时发生异常",StaticRes.class.getSimpleName()),e);
                }
                TypeDescription.Generic genericIStaticRes =
                        TypeDescription.Generic.Builder.parameterizedType(IStaticRes.class, idBoxedType, clazz).build();
                DynamicType.Unloaded<?> unloaded = new ByteBuddy()
                        .subclass(genericIStaticRes)
                        .name(packageName + "." + IStaticRes.class.getSimpleName() + idBoxedType.getSimpleName() + clazz.getSimpleName())
                        .method(ElementMatchers.isDeclaredBy(IStaticRes.class)).intercept(MethodDelegation.to(staticRes))
                        .make();
                // try {
                //     unloaded.saveIn(new File("target"));
                // } catch (IOException e) {
                //     e.printStackTrace();
                // }
                Class<?> subClass = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION).getLoaded();
                staticResDefinition.setvClass(clazz);
                try {
                    IStaticRes instance = (IStaticRes)subClass.newInstance();
                    staticRes.setFullFileName(staticResDefinition.getFullFileName());
                    staticResDefinition.setStaticRes(staticRes);
                    staticResDefinition.setiStaticRes(instance);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(String.format("[%s]<%s,%s>类型的bean实例化失败",IStaticRes.class,idBoxedType.getSimpleName(),clazz.getSimpleName()),e);
                }
            }
            StaticResDefinitionFactory staticResDefinitionFactory = beanFactory.getBean(StaticResDefinitionFactory.class);
            Map<String, StaticResDefinition> fullFileNameStaticResDefinition = staticResDefinitionFactory.getFullFileNameStaticResDefinition();
            String fullFileName = staticResDefinition.getFullFileName();
            if (fullFileNameStaticResDefinition.keySet().contains(fullFileName)){
                Class<?> oldClass = fullFileNameStaticResDefinition.get(fullFileName).getvClass();
                throw new RuntimeException(String.format("类[%s]与类[%s]对应同一个资源文件名[%s]",clazz,oldClass,fullFileName));
            }
            fullFileNameStaticResDefinition.put(fullFileName,staticResDefinition);
        });
    }

    /**
     * 扫描指定包下的所有.class文件
     * @param packageName 包名
     * @param beanFactory beanFactory
     */
    private void classesScan(String packageName, ConfigurableListableBeanFactory beanFactory) {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(packageName)
                + "/" + "**/*.class";
        Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources(packageSearchPath);
        } catch (IOException e) {
            throw new RuntimeException(String.format("无法获取指定包下[%s]的.class资源",packageName),e);
        }
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                try {
                    final MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    beanFactory.getBeansOfType(AbstractMetadataReaderPostProcessor.class).values().
                            forEach(metadataReaderPostProcessor -> metadataReaderPostProcessor.postProcessMetadataReader(metadataReader));
                } catch (IOException e) {
                    throw new RuntimeException(String.format("无法读取[%s]的Metadata",resource.getFilename()));
                }
            }
        }
    }
    protected String resolveBasePackage(String basePackage) {
        return convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }

}
