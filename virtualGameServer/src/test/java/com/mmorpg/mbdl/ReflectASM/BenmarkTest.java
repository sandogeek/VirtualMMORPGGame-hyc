package com.mmorpg.mbdl.ReflectASM;

import com.mmorpg.mbdl.framework.reflectASMwithUnsafe.ConstructorAccess;
import com.mmorpg.mbdl.framework.reflectASMwithUnsafe.FieldAccess;
import com.mmorpg.mbdl.framework.reflectASMwithUnsafe.MethodAccess;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BenmarkTest {

    /**
     * JDK反射调用方法
     * @throws Exception
     */
    @Test
    public void testJdkReflect() throws Exception {
        UserService target = new UserService();
        long start = System.currentTimeMillis();
        Method method = target.getClass().getMethod("update", int.class, String.class);
        for (int i = 0; i < 100000000; i++) {
            method.invoke(target, 1, "zhangsan");
        }
        long end = System.currentTimeMillis();
        System.out.println("timeout=" + (end - start));//809 753 880 875 816
    }

    /**
     * ReflectAsm反射调用方法
     * 用名称定位反射方法
     */
    @Test
    public void testReflectAsm4Name() {
        UserService target = new UserService();
        MethodAccess access = MethodAccess.get(UserService.class);//生成字节码的方式创建UserServiceMethodAccess
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            access.invoke(target, "update", 1, "zhangsan");
        }
        long end = System.currentTimeMillis();
        System.out.println("timeout=" + (end - start));//523 382 415 489 482
    }

    /**
     * ReflectAsm反射调用方法
     * 用方法和字段的索引定位反射方法，性能高
     */
    @Test
    public void testReflectAsm4Index() {
        UserService target = new UserService();
        MethodAccess access = MethodAccess.get(UserService.class);
        int index = access.getIndex("update", int.class, String.class);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            access.invoke(target, index, 1, "zhangsan");
        }
        long end = System.currentTimeMillis();
        System.out.println("timeout=" + (end - start));//12 15 23 14 24
    }

    /**
     * ReflectAsm反射来set/get字段值
     */
    @Test
    public void testFieldAccess() {
        UserService target = new UserService();
        FieldAccess fieldAccess = FieldAccess.getAccessUnsafe(target.getClass());
        int index = fieldAccess.getIndex("state");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 100000000; i++) {
            fieldAccess.set(target, index, 1);
            int state = (Integer)fieldAccess.get(target, "state");
        }
        stopWatch.stop();
        System.out.println("ReflectAsm字段设值 耗时:"+stopWatch.getTime());
    }

    @Test
    void testReflectFieldJDK() throws Exception {
        UserService target = new UserService();
        Field state = target.getClass().getDeclaredField("state");
        state.setAccessible(true);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 100000000; i++) {
            state.set(target, 1);
            int value = (Integer)state.get(target);
        }
        stopWatch.stop();
        System.out.println("jdk 反射字段设值 耗时:"+stopWatch.getTime());
    }

    /**
     * ReflectAsm反射来调用构造方法
     */
    @Test
    public void testConstructorAccess() {
        ConstructorAccess<UserService> constructorAccess = ConstructorAccess.get(UserService.class);
        UserService userService = constructorAccess.newInstance();
        System.out.println(userService);
    }

    /**
     * 查找方法的索引
     */
    @Test
    public void testIndex() {
        UserService target = new UserService();
        MethodAccess methodAccess = MethodAccess.get(target.getClass());
        int index = methodAccess.getIndex("update", int.class, String.class);
        System.out.println(index);
    }
}
