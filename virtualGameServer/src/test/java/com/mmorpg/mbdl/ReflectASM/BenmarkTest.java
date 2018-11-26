package com.mmorpg.mbdl.ReflectASM;

import com.mmorpg.mbdl.framework.reflectasm.withunsafe.ConstructorAccess;
import com.mmorpg.mbdl.framework.reflectasm.withunsafe.FieldAccess;
import com.mmorpg.mbdl.framework.reflectasm.withunsafe.MethodAccess;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BenmarkTest {
    @Test
    void injectStaticField() {
        UserService target = new UserService();
        FieldAccess fieldAccess = FieldAccess.accessUnsafe(target.getClass());
        int index = fieldAccess.getIndex("longStatic");
        long injectValue = 1444L;
        fieldAccess.setLong(target,index,injectValue);
        // StopWatch stopWatch = new StopWatch();
        // stopWatch.start();
        // int state = 0;
        // for (int i = 0; i < 100000000; i++) {
        //     fieldAccess.setInt(target, index, 1024);
        //     state = fieldAccess.getInt(target, index);
        // }
        // stopWatch.stop();
        // System.out.println(String.format("FieldAccess4Index字段设值 耗时:%s",stopWatch.getTime()));
        System.out.println(String.format("longStatic最终值:%s",fieldAccess.getLong(target,index)));
        Assertions.assertEquals(injectValue,UserService.getLongStatic());
    }

    @Test
    void 静态字段注入withoutUnsafe() {
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            UserService target = new UserService();
            FieldAccess fieldAccess = FieldAccess.access(target.getClass());
            int index = fieldAccess.getIndex("longStatic");
            long injectValue = 1444L;
            fieldAccess.setLong(target,index,injectValue);
            System.out.println(String.format("longStatic最终值:%s",fieldAccess.getLong(target,index)));
        });
    }

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
            method.invoke(target, 1, "sandogeek");
        }
        long end = System.currentTimeMillis();
        System.out.println("jdk反射方法调用耗时：" + (end - start));
    }

    /**
     * ReflectAsm反射调用方法
     * 用名称定位反射方法
     */
    @Test
    public void testReflectAsm4Name() {
        UserService target = new UserService();
        MethodAccess access = MethodAccess.access(UserService.class);//生成字节码的方式创建UserServiceMethodAccess
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            access.invoke(target, "update", 1, "sandogeek");
        }
        long end = System.currentTimeMillis();
        System.out.println("ReflectAsm4Name方法调用耗时：" + (end - start));
    }

    /**
     * ReflectAsm反射调用方法
     * 用方法和字段的索引定位反射方法，性能高
     */
    @Test
    public void testReflectAsm4Index() {
        UserService target = new UserService();
        MethodAccess access = MethodAccess.access(UserService.class);
        int index = access.getIndex("update", int.class, String.class);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            access.invoke(target, index, 1, "sandogeek");
        }
        long end = System.currentTimeMillis();
        System.out.println("ReflectAsm4Index方法调用耗时：" + (end - start));
    }

    /**
     * ReflectAsm反射来set/get字段值
     */
    @Test
    public void testFieldAccess4Index() {
        UserService target = new UserService();
        FieldAccess fieldAccess = FieldAccess.accessUnsafe(target.getClass());
        int index = fieldAccess.getIndex("statePrivate");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int state = 0;
        for (int i = 0; i < 100000000; i++) {
            fieldAccess.setInt(target, index, 1024);
            state = fieldAccess.getInt(target, index);
        }
        stopWatch.stop();
        System.out.println(String.format("FieldAccess4Index字段设值 耗时:%s",stopWatch.getTime()));
        // System.out.println(String.format("state最终值:%s",state));
        Assertions.assertEquals(target.getStatePrivate(),state);
    }

    @Test
    void testFieldAccess4IndexInteger() {
        UserService target = new UserService();
        FieldAccess fieldAccess = FieldAccess.accessUnsafe(target.getClass());
        int index = fieldAccess.getIndex("stateInteger");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Integer state = 0;
        for (int i = 0; i < 100000000; i++) {
            fieldAccess.setObject(target, index, 1024);
            state = (Integer)fieldAccess.getObject(target, index);
        }
        stopWatch.stop();
        System.out.println(String.format("FieldAccess4IndexInteger字段设值 耗时:%s",stopWatch.getTime()));
        // System.out.println(String.format("state最终值:%s",state));
        Assertions.assertEquals(target.getStateInteger(),state);
    }

    @Test
    void testReflectFieldJDK() throws Exception {
        UserService target = new UserService();
        Field state = target.getClass().getDeclaredField("stateInteger");
        state.setAccessible(true);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (int i = 0; i < 100000000; i++) {
            state.set(target, 1024);
            int value = (Integer)state.get(target);
        }
        stopWatch.stop();
        System.out.println("jdk 反射字段设值 耗时:"+stopWatch.getTime());
        Assertions.assertEquals(target.getStateInteger(),new Integer(1024));
    }

    @Test
    void accessWithoutUnsafe() {
        UserService target = new UserService();
        FieldAccess fieldAccess = FieldAccess.access(target.getClass());
        int index = fieldAccess.getIndex("state");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int state = 0;
        for (int i = 0; i < 100000000; i++) {
            fieldAccess.setInt(target, index, 1024);
            state = fieldAccess.getInt(target, index);
        }
        stopWatch.stop();
        System.out.println(String.format("accessWithoutUnsafe字段设值 耗时:%s",stopWatch.getTime()));
        // System.out.println(String.format("state最终值:%s",state));
        Assertions.assertEquals(target.getState(),state);
    }


    @Test
    void testFieldAccess4Name() {
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            UserService target = new UserService();
            FieldAccess fieldAccess = FieldAccess.accessUnsafe(target.getClass());
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            Integer state = 0;
            for (int i = 0; i < 100000000; i++) {
                // error调用，使用了setObject设置基本值
                fieldAccess.setObject(target, "state", 1024);
                state = (Integer)fieldAccess.getObject(target, "state");
            }
            stopWatch.stop();
            System.out.println(String.format("FieldAccess4Name字段设值 耗时:%s",stopWatch.getTime()));
            System.out.println(String.format("state最终值:%s",target.state));
        });
        UserService target = new UserService();
        FieldAccess fieldAccess = FieldAccess.accessUnsafe(target.getClass());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        int state = 0;
        for (int i = 0; i < 100000000; i++) {
            // error调用，使用了setObject设置基本值
            fieldAccess.setInt(target, fieldAccess.getIndex("state"), 1024);
            state = fieldAccess.getInt(target, fieldAccess.getIndex("state"));
        }
        stopWatch.stop();
        System.out.println(String.format("FieldAccess4Name字段设值 耗时:%s",stopWatch.getTime()));
        // System.out.println(String.format("state最终值:%s",target.state));
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
        MethodAccess methodAccess = MethodAccess.access(target.getClass());
        int index = methodAccess.getIndex("update", int.class, String.class);
        System.out.println(index);
    }
}
