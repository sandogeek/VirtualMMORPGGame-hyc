package com.mmorpg.mbdl.ByteBuddy;

// import com.mmorpg.mbdl.bussiness.register.cache.PlayerAccountEntityService;

import com.mmorpg.mbdl.ByteBuddy.foo.Bar;
import com.mmorpg.mbdl.bussiness.register.entity.PlayerAccountEntity;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Set;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class ByteBuddyTest {
    private static final Logger logger= LoggerFactory.getLogger(ByteBuddyTest.class);

    @Test
    void methodMatcher() {
        try {
            DynamicType.Builder<Bar> builder = new ByteBuddy()
                    .subclass(Bar.class);
            // 方法选择器以stack形式组织，并且找到符合的选择器后就不会继续往后找，所以最后加入的最先被应用，于是更具体的条件应该放到更后面，
            DynamicType.Unloaded<Bar> unloaded = builder
                    .method(isDeclaredBy(Bar.class)).intercept(FixedValue.value("One!"))
                    .method(named("foo")).intercept(FixedValue.value("Two!"))
                    // 被放到静态字段three中，getLoaded时TypeInitializer初始化，不使用bytebuddy加载需要自己调用TypeInitializer，才能初始化该字段
                    .method(named("foo").and(takesArguments(1))).intercept(FixedValue.reference("three","three"))
                    .make();
            unloaded.saveIn(new File("target"));
            Bar bar = unloaded.load(getClass().getClassLoader())
                    .getLoaded()
                    .newInstance();
            logger.info(bar.foo(new byte[0]));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void loadingClass() throws Exception {
        Class<?> type = new ByteBuddy()
                .subclass(Object.class)
                .defineField("defineByByteBuddy", int.class,
                        Modifier.PUBLIC|Modifier.STATIC|Modifier.FINAL)
                .make()
                .load(getClass().getClassLoader())
                .getLoaded();
        ClassLoader typeClassLoader = type.getClassLoader();
        // Enumeration<URL> resources = type.getClassLoader().getResources("");
        // while (resources.hasMoreElements()){
        //     logger.info("{}",resources.nextElement());
        // }
        // logger.info("{}",resources);
        // type.getClassLoader().getResourceAsStream(type.getName()).
    }

    @Test
    void typePool() throws Exception {
        // TypePool typePool = TypePool.Default.of(getClass().getClassLoader());
        /*TypePool typePool = TypePool.Default.ofSystemLoader();
        TypePool.Resolution describe = typePool.describe("com.mmorpg.mbdl.ByteBuddy.foo.Bar");
        TypeDescription typeDescription = describe.resolve();
        DynamicType.Unloaded<Object> unloaded = new ByteBuddy().redefine(typeDescription, ClassFileLocator.ForClassLoader.of(getClass().getClassLoader()))
                // .defineField("defineByByteBuddy", int.class, Visibility.PUBLIC, Ownership.STATIC, FieldManifestation.FINAL)
                .defineField("defineByByteBuddy", int.class,
                        Opcodes.ACC_PUBLIC|Opcodes.ACC_STATIC|Opcodes.ACC_FINAL)
                .make();
        // Class<?> aClass = unloaded.load(getClass().getClassLoader(),ClassLoadingStrategy.Default.CHILD_FIRST).getLoaded();
        unloaded.load(ClassLoader.getSystemClassLoader());
        Class<?> loadClass = getClass().getClassLoader().loadClass("com.mmorpg.mbdl.ByteBuddy.foo.Bar");
        Field[] declaredFields = loadClass.getDeclaredFields();*/
        // unloaded.saveIn(new File("target"));
        logger.info("");
    }

    @Test
    void typePool官方版本() throws Exception {
        // TypePool typePool = TypePool.Default.ofSystemLoader();
        // new ByteBuddy()
        //         .redefine(typePool.describe("com.mmorpg.mbdl.ByteBuddy.foo.Bar").resolve(), // do not use 'Bar.class'
        //                 ClassFileLocator.ForClassLoader.ofSystemLoader())
        //         .defineField("qux", String.class) // we learn more about defining fields later
        //         .make()
        //         .load(ClassLoader.getSystemClassLoader());
        // Assertions.assertNotNull(Bar.class.getDeclaredField("qux"));
    }

    @Test
    void namingStrategy() {
        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
                .with(new NamingStrategy.SuffixingRandom("sando"))
                .subclass(Object.class)
                .make();
        Class<?> loaded = dynamicType.load(getClass().getClassLoader()).getLoaded();
        logger.info("{}",loaded.getName());
    }

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
        //         .intercept(MethodDelegation.to(FindOneInterceptor.class)) //使用 FindOneInterCeptor 中的实现
        //         .annotateType(AnnotationDescription.Builder.ofType(Scope.class).define("value", "Session").build())
        //         .make();
        // 在 Maven 项目中，写类文件在 target/classes/cc/unmi/UserRepository.class 中
        // unloadedType.saveIn(new File("target/classes"));
        //可以这样生成字节码得到 Class 实例来加载使用
        //Class<?> subClass = unloadedType.load(Main.class.getClassLoader(),
        //    ClassLoadingStrategy.Default.WRAPPER).getLoaded();

        // Class<Repository<String>> repositoryClass = (Class<Repository<String>>) Class.forName("");
        // System.out.println(repositoryClass.getAnnotation(Scope.class).value()); //输出 Session
        //
        // Repository<String> repository = repositoryClass.newInstance();
        // System.out.println(repository.findOne(7792));
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
