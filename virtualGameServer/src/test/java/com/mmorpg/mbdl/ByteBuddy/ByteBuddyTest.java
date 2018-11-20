package com.mmorpg.mbdl.ByteBuddy;

// import com.mmorpg.mbdl.bussiness.register.cache.PlayerAccountEntityService;
import com.mmorpg.mbdl.bussiness.register.entity.PlayerAccountEntity;
import net.bytebuddy.description.type.TypeDescription;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ResolvableType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class ByteBuddyTest {
    private static final Logger logger= LoggerFactory.getLogger(ByteBuddyTest.class);
    @Test
    public void genericSubClassTest(){
        //泛型类型需要这么声明参数类型
        TypeDescription.Generic genericSuperClass =
                TypeDescription.Generic.Builder.parameterizedType(JpaRepository.class, PlayerAccountEntity.class,Long.class).build();
        // //new ByteBuddy().subclass(Repository.class) //简单非泛型类可以这么做
        // DynamicType.Unloaded<?> unloadedType = new ByteBuddy().makeInterface(genericSuperClass)
        //         .name(PlayerAccountEntityService.class.getPackage().getName().concat(".dao.").concat(PlayerAccountEntity.class.getSimpleName()+"Repository"))
        //         .method(ElementMatchers.named("findOne"))  //ElementMatchers 提供了多种方式找到方法
        //         //.intercept(FixedValue.value("Yanbin"))   //最简单的方式就是返回一个固定值
        //         .intercept(MethodDelegation.to(FindOneInterceptor.class)) //使用 FindOneInterCeptor 中的实现，定义在下方
        //         .annotateType(AnnotationDescription.Builder.ofType(Scope.class).define("value", "Session").build())
        //         .make();
        // 在 Maven 项目中，写类文件在 target/classes/cc/unmi/UserRepository.class 中
        // unloadedType.saveIn(new File("target/classes"));
        //可以这样生成字节码得到 Class 实例来加载使用
        //Class<?> subClass = unloadedType.load(Main.class.getClassLoader(),
        //    ClassLoadingStrategy.Default.WRAPPER).getLoaded();

        // Class<Repository<String>> repositoryClass = (Class<Repository<String>>) Class.forName("cc.unmi.UserRepository");
        // System.out.println(repositoryClass.getAnnotation(Scope.class).value()); //输出 Session
        //
        // Repository<String> repository = repositoryClass.newInstance();
        // System.out.println(repository.findOne(7792));  //输出 http://yanbin.blog/?p=7792
    }

    @Test
    public void loadClass() throws IOException {
        // TypeDescription.Generic genericSuperClass =
        //         TypeDescription.Generic.Builder.parameterizedType(IStorage.class, Long.class, PlayerAccountEntity.class).build();
        // String packageName = PlayerAccountEntityService.class.getPackage().getName();
        // String classFullName = packageName.substring(0,packageName.lastIndexOf(".")).concat(".dao.").concat(PlayerAccountEntity.class.getSimpleName() + "Repository");
        // DynamicType.Unloaded<?> type = new ByteBuddy()
        //         .makeInterface(genericSuperClass)
        //         .name(classFullName)
        //         .annotateType(AnnotationDescription.Builder.ofType(ByteBuddyGenerated.class).build())
        //         .make();
        // // 在 Maven 项目中，写类文件在 target/classes/cc/unmi/UserRepository.class 中
        // type.saveIn(new File("target/classes"));
    }
    // 尝试获取所有dao包下的实现了JpaRepo接口的类
    @Test
    public void getAllDaoJpaRepo(){
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.mmorpg.mbdl.bussiness"))
                .setScanners(new SubTypesScanner(), new TypeAnnotationsScanner())
                .filterInputsBy(new FilterBuilder().include(".*\\.dao.*")));
        Set<Class<? extends JpaRepository>> jpaRepoInterfaceSet = reflections.getSubTypesOf(JpaRepository.class);

        HashMap<ResolvableType,Class<? extends JpaRepository>> resolvableTypeClassHashMap = new HashMap<>(50);
        // 初始化resolvableTypeClassHashMap
        for (Class<? extends JpaRepository> clazz :
                jpaRepoInterfaceSet) {
            for (ResolvableType resolvableType :
                    ResolvableType.forClass(clazz).getInterfaces()) {
                resolvableTypeClassHashMap.put(resolvableType,clazz);
            }
        }
        HashMap<Object,ResolvableType> objectResolvableTypeHashMap = new HashMap<>(40);
        for (ResolvableType resolvableType:
             resolvableTypeClassHashMap.keySet()) {
            if (objectResolvableTypeHashMap.keySet().contains(resolvableType.getSource())){
                Class<? extends JpaRepository> oldClass = resolvableTypeClassHashMap.get(objectResolvableTypeHashMap.get(resolvableType.getSource()));
                Class<? extends JpaRepository> newClass = resolvableTypeClassHashMap.get(resolvableType);
                throw new RuntimeException(String.format("dao类[%s]与类[%s]有相同的泛型父类接口%s",oldClass.getSimpleName(),newClass.getSimpleName(),resolvableType.toString()));
            }
            objectResolvableTypeHashMap.put(resolvableType.getSource(),resolvableType);
        }
    }
    
}
