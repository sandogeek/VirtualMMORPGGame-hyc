package com.mmorpg.mbdl.ByteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.Super;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.*;


/**
 * @author Sando Geek
 * @since v1.0
 **/
public class MethodDelegationThisTest {
    private static final Logger logger= LoggerFactory.getLogger(MethodDelegationThisTest.class);
    @Test
    public void testThis() throws Exception {
        DynamicType.Unloaded<Foo> fooUnloaded = new ByteBuddy()
                .subclass(Foo.class)
                .name(Foo.class.getName()+"Sub")
                .method(isDeclaredBy(Foo.class))
                .intercept(MethodDelegation.to(Bar.class))
                .make();
        fooUnloaded.saveIn(new File("target"));
        DynamicType.Loaded<Foo> loaded = fooUnloaded.load(Foo.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER);
        Foo instance = loaded.getLoaded().getDeclaredConstructor().newInstance();
        Assertions.assertEquals(instance.foo(), (Object) instance);
    }

    @Test
    void 利用this完成AOP() {
        DynamicType.Unloaded<Source> unloaded = new ByteBuddy()
                .subclass(Source.class)
                .name(Source.class.getName()+"Sub")
                .method(named("hello")).intercept(MethodDelegation.to(Target.class))
                .method(named("getInt").and(takesArguments(0))).intercept(MethodDelegation.to(Target.class))
                .defineMethod("myMethod",int.class, Modifier.PUBLIC|Modifier.STATIC).withParameters(int.class).intercept(MethodDelegation.to(Target.class))
                .make();
        try {
            unloaded.saveIn(new File("target"));
            Source source = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                    .getLoaded()
                    .newInstance();
            String s = source.hello("World");
            logger.info(s);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Source {
        int k = 2;
        @Transactional
        public String hello(String name) { return null; }
        public int getInt() { return 1; }
    }

    public static class Target {
        public static String hello(@This Source source, @Argument(0) String name) {
            // @This,所以访问的是增强后的方法，所以anInt应该为2
            int anInt = source.getInt();
            return "Hello " + name + "! int= "+anInt+" k = "+source.k;
        }
        public static int getInt(@Super Source source, @SuperCall Callable<Integer> zuper) throws Exception {
            return 2+zuper.call();
        }

        public static int myMethod(int a){
            return a;
        }
        // public static String intercept(Object o) { return o.toString(); }
    }

    public static class Foo {

        public Object foo() {
            return null;
        }
    }

    public static class Bar {

        public static Object qux(@This Foo foo) {
            return foo;
        }

        public static Object baz(@This Void v) {
            return v;
        }
    }
}
