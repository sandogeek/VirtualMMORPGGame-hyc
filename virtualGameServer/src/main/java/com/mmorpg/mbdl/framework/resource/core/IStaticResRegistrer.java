package com.mmorpg.mbdl.framework.resource.core;

import com.google.common.base.Preconditions;
import com.mmorpg.mbdl.framework.resource.annotation.Id;
import com.mmorpg.mbdl.framework.resource.annotation.ResDef;
import com.mmorpg.mbdl.framework.resource.facade.IStaticRes;
import com.mmorpg.mbdl.framework.storage.annotation.ByteBuddyGenerated;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
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
import java.util.*;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;

/**
 * 生成各种IStaticRes类型的对象并注入到spring容器中
 * TODO 面向资源读取接口IResReader编程，提高可拓展性
 * @author Sando Geek
 * @since v1.0
 **/
public class IStaticResRegistrer implements ImportBeanDefinitionRegistrar {
    private String packageToScan;
    private String suffix;
    private Class<? extends IStaticRes> baseClass;
    // 完整文件名 = 文件名加后缀名 -> StaticResDefinition
    private Map<String,StaticResDefinition> fileFullName2StaticResDefinition;
    // /** 资源类clazz -> 对应的baseClass的子类 */
    // Map<Class,Class> resDefClazz2IStaticResSubClazz;


    public void setPackageToScan(String packageToScan) {
        Preconditions.checkArgument(packageToScan==null,"静态资源未配置包扫描路径");
        this.packageToScan = packageToScan;
    }

    public void setSuffix(String suffix) {
        Preconditions.checkArgument(packageToScan==null,"静态资源未配置默认静态资源后缀");
        this.suffix = suffix;
    }

    public void setBaseClass(Class<? extends IStaticRes> baseClass) {
        this.baseClass = baseClass;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 生成所有需要注入spring容器的IStaticRes类的子类
        Map<Class, Class> resDefClazz2IStaticResSubClazz = generateBaseClassSubClass(getResDefClasses(packageToScan));
        if (resDefClazz2IStaticResSubClazz != null){
            resDefClazz2IStaticResSubClazz.forEach((key,value)->{
                key.n
            });
        }
    }

    /**
     * 每个资源类生成一个对应的baseClass的子类
     * @param classes 类名数组
     * @return 资源类clazz -> 对应的baseClass的子类,如果classNames长度为0，返回null
     */
    private Map<Class,Class> generateBaseClassSubClass(Set<Class> classes){
        if (classes.size() == 0) {
            return null;
        }
        Map<Class,Class> result = new HashMap<>(64);
        classes.stream().forEach((clazz)->{
            Set<Field> fields = getAllFields(clazz, withAnnotation(Id.class));
            if (fields.size() != 1){
                throw new RuntimeException(String.format("资源类[%s]包含多个@Id注解的字段",clazz.getSimpleName()));
            }
            Class<?> idFieldType = fields.toArray(new Field[0])[0].getType();
            TypeDescription.Generic genericSuperClass =
                    TypeDescription.Generic.Builder.parameterizedType(baseClass, idFieldType, clazz).build();
            String packageName = IStaticRes.class.getPackage().getName();
            Class<?> subClass = new ByteBuddy()
                    .makeInterface(genericSuperClass)
                    .name(packageName + "." + baseClass.getSimpleName() + idFieldType.getSimpleName() + clazz.getSimpleName())
                    .annotateType(AnnotationDescription.Builder.ofType(ByteBuddyGenerated.class).build())
                    .make().load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION).getLoaded();
            result.put(clazz,subClass);
        });
        return result;
    }

    /**
     * 根据包名获取被ResDef注解的所有class对象
     * @param packageName
     * @return String[]
     * @throws IOException
     */
    private Map<String,StaticResDefinition> getResDefClasses(String packageName) {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        Map<String,StaticResDefinition> result = new HashMap<>(64);
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(packageName)
                + "/" + "**/*.class";
        Resource[] resources = new Resource[0];
        try {
            resources = resourcePatternResolver.getResources(packageSearchPath);
        } catch (IOException e) {
            throw new RuntimeException(String.format("无法获取指定包下[%s]的.class资源",packageName));
        }
        Class clz;
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
                    ResDef resDef = resClazz.getAnnotation(ResDef.class);
                    String fullFileName = StringUtils.isEmpty(resDef.value())?resDef.value():resClazz.getSimpleName();
                    // if (!suffix.equals(resDef.getSuffix())){
                    //     continue;
                    // }
                    result.put(fullFileName,new StaticResDefinition(resClazz));
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
