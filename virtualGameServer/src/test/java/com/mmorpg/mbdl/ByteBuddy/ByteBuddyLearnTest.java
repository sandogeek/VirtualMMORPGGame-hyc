package com.mmorpg.mbdl.ByteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class ByteBuddyLearnTest {
    // 处理字段和方法
    @Test
    public void fieldAndMethod() throws Exception {
        Class<?> clz = new ByteBuddy(ClassFileVersion.ofThisVm(ClassFileVersion.JAVA_V8))
                .subclass(Object.class)
                .name("bytebuddy.learn.HandleFiedAndMethod")
                .method(named("toString").and(returns(String.class)).and(takesArguments(0)))
                .intercept(FixedValue.value("custom"))
                .make().load(getClass().getClassLoader()).getLoaded();
        String s = clz.newInstance().toString();
        System.out.println(s);
    }
    // 在.class对象生成前重定义.class文件
    @Test
    public void redifineTest() throws Exception{
        TypePool typePool = TypePool.Default.of(getClass().getClassLoader());
        DynamicType.Unloaded<?> objectUnloaded = new ByteBuddy()
                .redefine(typePool.describe("com.mmorpg.mbdl.ByteBuddy.foo.Bar").resolve(), // do not use 'Bar.class'
                        ClassFileLocator.ForClassLoader.of(getClass().getClassLoader()))
                .defineField("qux", String.class) // we learn more about defining fields later
                .make();
        // objectUnloaded.saveIn(new File("target/test-classes"));
        // 由于获取objectUnloaded的过程加载了com.mmorpg.mbdl.ByteBuddy.foo.Bar.class，所以这里要换个ClassLoader才能加载
        Class<?> loaded = objectUnloaded.load(ClassLoader.getSystemClassLoader().getParent()).getLoaded();
        Assertions.assertNotNull(loaded.getDeclaredField("qux"));
    }
}
