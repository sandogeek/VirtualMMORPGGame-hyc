package com.mmorpg.mbdl;

import com.mmorpg.mbdl.framework.common.generator.PacketIdTsGenerator;
import com.mmorpg.mbdl.framework.common.utils.FileUtils;
import com.mmorpg.mbdl.framework.common.utils.SpringPropertiesUtil;
import com.mmorpg.mbdl.framework.communicate.websocket.model.AbstractPacket;
import com.mmorpg.mbdl.framework.communicate.websocket.server.WebSocketServer;
import com.mmorpg.mbdl.framework.resource.core.IStaticResUtil;
import com.mmorpg.mbdl.framework.storage.core.IStorage;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.SystemPropertyUtils;

import javax.persistence.Entity;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;

/**
 * 启动spring容器,为了使用{@link SpringPropertiesUtil},部分启动逻辑放置在{@link EnhanceStarter#init()}中
 * @author Sando Geek
 */
public class Start {
    private static Logger logger= LoggerFactory.getLogger(Start.class);
    /** @ Entity 注解的类所在的包 */
    private static String ENTITY_CLZ_PACKAGE = "com.mmorpg.mbdl.bussiness";

    public static void  main(String[] args) throws Exception {
        generateDaoInterfaces();
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        PacketIdTsGenerator.getInstance().generatePacketIdTs();
        removeAbstractPacketBean(ctx);
        logger.info("开始启动WebSocket服务器...");
        WebSocketServer webSocketServer = WebSocketServer.getInstance();
        webSocketServer.bind(WebSocketServer.PORT);
    }

    /**
     * 生成缺失的dao接口
     */
    static void generateDaoInterfaces(){
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(ENTITY_CLZ_PACKAGE)
                + "/" + "**/*.class";
        Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources(packageSearchPath);
        } catch (IOException e) {
            throw new RuntimeException(String.format("无法获取指定包下[%s]的.class资源",ENTITY_CLZ_PACKAGE),e);
        }
        Map<Class<?>,String> entityClz2DaoInterfaceName = new HashMap<>(32);

        initEntityClz2DaoInterfaceNameKeys(getMetadataStream(metadataReaderFactory, resources),entityClz2DaoInterfaceName);
        initEntityClz2DaoInterfaceNameValues(getMetadataStream(metadataReaderFactory, resources),entityClz2DaoInterfaceName);

        Random random = new Random();
        for (Class clz : entityClz2DaoInterfaceName.keySet()){
            // 已有对应接口的不需要生成
            // if (entityClz2DaoInterfaceName.get(clz)!=null){
            //     continue;
            // }
            ResolvableType genericIStorage = ResolvableType.forType(clz.getGenericInterfaces()[0]);
            // 主键类
            Class<?> pkClass = genericIStorage.getGeneric(0).resolve();
            TypeDescription.Generic genericSuperClass =
                    TypeDescription.Generic.Builder.parameterizedType(IStorage.class, pkClass, clz).build();
            String clzPackageName = clz.getPackage().getName();
            String classFullName = clzPackageName.substring(0,clzPackageName.lastIndexOf("."))
                    .concat(".dao.").concat(IStorage.class.getSimpleName()).concat(pkClass.getSimpleName()).concat(clz.getSimpleName())
                    .concat(random.nextInt(50)+"")
                    .concat("$$ByteBuddy");
            DynamicType.Unloaded<?> unloadedDao = new ByteBuddy()
                    .makeInterface(genericSuperClass)
                    .name(classFullName)
                    .make();
            try {
                unloadedDao.saveIn(new File(IStaticResUtil.getClassPathSuffixWith("/classes")));
            } catch (IOException e) {
                throw new RuntimeException(String.format("保存Dao接口[%s]发生异常",classFullName),e);
            }
            // logger.debug("");
        }

        // logger.debug("");
    }

    private static Stream<MetadataReader> getMetadataStream(MetadataReaderFactory metadataReaderFactory, Resource[] resources) {
        Stream<MetadataReader> metadataReaderStream = Arrays.stream(resources).filter(Resource::isReadable).map(resource -> {
            MetadataReader metadataReader;
            try {
                metadataReader = metadataReaderFactory.getMetadataReader(resource);
            } catch (IOException e) {
                throw new RuntimeException("生成缺失的dao接口过程中发生异常", e);
            }
            return metadataReader;
        }).filter(Objects::nonNull);
        return metadataReaderStream;
    }

    /**
     * 初始化entityClz2DaoInterfaceName中的键
     * @param metadataReaderStream MetadataReader流
     * @param entityClz2DaoInterfaceName 带@Entity注解的类 -> 对应的Dao接口的名称
     */
    private static void initEntityClz2DaoInterfaceNameKeys(Stream<MetadataReader> metadataReaderStream,Map<Class<?>,String> entityClz2DaoInterfaceName) {
        metadataReaderStream.filter(metadataReader -> {
            AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
            if (annotationMetadata.hasAnnotation(Entity.class.getName())){
                return true;
            }
            return false;
        }).forEach(metadataReader -> {
            try {
                Class<?> entityClz = Class.forName(metadataReader.getClassMetadata().getClassName());
                entityClz2DaoInterfaceName.put(entityClz,null);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(String.format("无法加载类[%s]",metadataReader.getClassMetadata().getClassName()),e);
            }
        });
    }

    /**
     * 初始化entityClz2DaoInterfaceName中的值
     * @param metadataReaderStream MetadataReader流
     * @param entityClz2DaoInterfaceName 带@Entity注解的类 -> 对应的Dao接口的名称
     */
    private static void initEntityClz2DaoInterfaceNameValues(Stream<MetadataReader> metadataReaderStream,Map<Class<?>,String> entityClz2DaoInterfaceName) {
        metadataReaderStream.filter(metadataReader -> {
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            String[] interfaceNames = classMetadata.getInterfaceNames();
            for (String interfaceName : interfaceNames) {
                if (interfaceName.equals(IStorage.class.getName())) {
                    return true;
                }
            }
            return false;
        }).forEach(metadataReader -> {
            try {
                Class<?> daoClazz = Class.forName(metadataReader.getClassMetadata().getClassName());
                // if (daoClazz.getSimpleName().endsWith("$$ByteBuddy")){
                //
                // }
                ResolvableType genericIStorage = ResolvableType.forType(daoClazz.getGenericInterfaces()[0]);
                Class<?> iEntityClass = genericIStorage.getGeneric(1).resolve();
                String daoClzName = entityClz2DaoInterfaceName.put(iEntityClass, daoClazz.getName());
                // !=null说明是后来新增了自定义的dao接口，此时要清除ByteBuddy生成的接口的.class文件
                if (daoClzName!=null){
                    String dir = IStaticResUtil.getClassPathSuffixWith("/classes")+"/"
                            + daoClzName.substring(0,daoClzName.lastIndexOf(".")).replace(".","/");
                    FileUtils.clearByFileFilter(dir,false,FileUtils.withSuffix("$$ByteBuddy.class"));
                }
                logger.debug("");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(String.format("无法加载类[%s]",metadataReader.getClassMetadata().getClassName()),e);
            }
        });
    }

    static String resolveBasePackage(String basePackage) {
        return convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }

    /**
     * 在Spring IOC启动后去除容器中AbstractPacket的单例
     * @param ctx spring上下文
     */
    static void removeAbstractPacketBean(ConfigurableApplicationContext ctx){
        for (String beanName :
                ctx.getBeanNamesForType(AbstractPacket.class)) {
            BeanDefinitionRegistry beanDefReg = (DefaultListableBeanFactory)ctx.getBeanFactory();
            beanDefReg.removeBeanDefinition(beanName);
        }
    }
}
