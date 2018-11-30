package com.mmorpg.mbdl.framework.resource.core;

import com.google.common.base.Preconditions;
import com.mmorpg.mbdl.EnhanceStarter;
import com.mmorpg.mbdl.framework.common.utils.SpringPropertiesUtil;
import com.mmorpg.mbdl.framework.resource.annotation.Id;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;
import com.mmorpg.mbdl.framework.resource.facade.AbstractBeanFactoryAwareResResolver;
import com.mmorpg.mbdl.framework.resource.facade.IResResolver;
import com.mmorpg.mbdl.framework.resource.facade.IStaticRes;
import com.mmorpg.mbdl.framework.resource.impl.StaticRes;
import com.mmorpg.mbdl.framework.storage.annotation.ByteBuddyGenerated;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;

/**
 * 静态资源处理器
 * @author Sando Geek
 * @since v1.0
 **/
public class StaticResHandler implements BeanFactoryPostProcessor {
    private static Logger logger = LoggerFactory.getLogger(StaticResHandler.class);
    private String packageToScan;
    private String suffix;
    // 完整文件名 = 文件名加后缀名 -> StaticResDefinition
    private Map<String,StaticResDefinition> fileFullName2StaticResDefinition;
    // /** 资源类clazz -> 对应的baseClass的子类 */
    // Map<Class,Class> resDefClazz2IStaticResSubClazz;

    public void setPackageToScan(String packageToScan) {
        Preconditions.checkArgument(packageToScan!=null,"静态资源未配置包扫描路径");
        this.packageToScan = packageToScan;
    }

    public void setSuffix(String suffix) {
        Preconditions.checkArgument(suffix!=null,"静态资源未配置默认静态资源后缀");
        this.suffix = suffix;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        AbstractBeanFactoryAwareResResolver.setBeanFactory(beanFactory);
        // 使SpringPropertiesUtil可用于Init中
        beanFactory.getBean(SpringPropertiesUtil.class);
        EnhanceStarter.setBeanFactory(beanFactory);
        EnhanceStarter.init();
        Map<Class, StaticResDefinition> classStatic2ResDefinitionMap = getResDefClasses(packageToScan);
        init(classStatic2ResDefinitionMap,beanFactory);
        beanFactory.getBeansOfType(IResResolver.class).forEach((key,value) -> value.resolve());
    }

    /**
     * 初始化
     *
     * <p>初始化StaticResDefinitionFactory,每个表格型资源类生成一个对应的IStaticRes的子类,并存放到StaticResDefinition</p>
     * @param class2StaticResDefinitionMap
     * @return 资源类clazz -> 对应的baseClass的子类,如果集合大小为0，返回null
     */
    private void init(Map<Class,StaticResDefinition> class2StaticResDefinitionMap,ConfigurableListableBeanFactory beanFactory){
        if (class2StaticResDefinitionMap.size() == 0) {
            return;
        }
        class2StaticResDefinitionMap.keySet().forEach((clazz)->{
            Set<Field> fields = getAllFields(clazz, withAnnotation(Id.class));
            if (fields.size() != 1){
                throw new RuntimeException(String.format("资源类[%s]包含多个@Id注解的字段",clazz.getSimpleName()));
            }
            Field idField = fields.toArray(new Field[0])[0];
            StaticResDefinition staticResDefinition = class2StaticResDefinitionMap.get(clazz);
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

            TypeDescription.Generic genericSuperClass =
                    TypeDescription.Generic.Builder.parameterizedType(StaticRes.class, idBoxedType, clazz).build();
            String packageName = clazz.getPackage().getName();
            Class<?> subClass = new ByteBuddy()
                    .subclass(genericSuperClass)
                    .name(packageName + "." + StaticRes.class.getSimpleName() + idBoxedType.getSimpleName() + clazz.getSimpleName())
                    .annotateType(AnnotationDescription.Builder.ofType(ByteBuddyGenerated.class).build())
                    .make().load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION).getLoaded();
            staticResDefinition.setvClass(clazz);
            try {
                staticResDefinition.setStaticRes((StaticRes)subClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(String.format("[%s]<%s,%s>类型的bean实例化失败",IStaticRes.class,idBoxedType,clazz.getSimpleName()));
            }
            StaticResDefinitionFactory staticResDefinitionFactory = beanFactory.getBean(StaticResDefinitionFactory.class);
            staticResDefinitionFactory.getFullFileNameStaticResDefinition().put(staticResDefinition.getFullFileName(),staticResDefinition);
        });
    }

    /**
     * 根据包名获取被ResDef注解的所有class对象
     * @param packageName
     * @return Map<Class,StaticResDefinition>
     * @throws IOException
     */
    private Map<Class,StaticResDefinition> getResDefClasses(String packageName) {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        Map<Class,StaticResDefinition> result = new HashMap<>(64);
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(packageName)
                + "/" + "**/*.class";
        Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources(packageSearchPath);
        } catch (IOException e) {
            throw new RuntimeException(String.format("无法获取指定包下[%s]的.class资源",packageName));
        }
        Class clz;
        // TODO 实现MetadataReaderPostProcessor,避免多次循环.class文件Resource，
        // 思路：第一次遍历找出所有实现了MetadataReaderPostProcessor的.class文件，实例化后在第二次遍历时，调用其postProcess方法
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader = null;
                try {
                    metadataReader = metadataReaderFactory.getMetadataReader(resource);
                } catch (IOException e) {
                    throw new RuntimeException(String.format("无法读取[%s]的Metadata",resource.getFilename()));
                }
                AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                if (!annotationMetadata.hasAnnotation(ResDef.class.getName())) {
                    continue;
                }
                try {
                    Class<?> resClazz = Class.forName(metadataReader.getClassMetadata().getClassName());
                    result.put(resClazz,new StaticResDefinition());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(String.format("无法加载类[%s]",metadataReader.getClassMetadata().getClassName()));
                }

            }
        }
        return result;
    }
    protected String resolveBasePackage(String basePackage) {
        return convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }

}
