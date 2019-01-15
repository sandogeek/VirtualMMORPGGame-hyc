package com.mmorpg.mbdl.ByteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.This;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;

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
        Target target = new Target();
        DynamicType.Unloaded<ISource> unloaded = new ByteBuddy(ClassFileVersion.JAVA_V8)
                .subclass(ISource.class)
                .name(ISource.class.getName()+"Sub")
                .method(isDeclaredBy(ISource.class)).intercept(MethodDelegation.to(target))
                // .method(named("hello")).intercept(MethodDelegation.to(target))
                .method(named("getInt").and(takesArguments(0))).intercept(MethodDelegation.toField("test"))
                .defineMethod("myMethod",int.class, Modifier.PUBLIC).withParameters(int.class).intercept(MethodDelegation.to(target))
                .make();
        try {
            unloaded.saveIn(new File("target"));
            ISource iSource = unloaded.load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                    .getLoaded()
                    .newInstance();
            logger.info(iSource.hello("World"));
        } catch (InstantiationException | IllegalAccessException | IOException e) {
            e.printStackTrace();
        }
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
