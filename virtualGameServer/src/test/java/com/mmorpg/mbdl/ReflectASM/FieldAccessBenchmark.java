package com.mmorpg.mbdl.ReflectASM;

import com.mmorpg.mbdl.framework.reflectasm.withunsafe.FieldAccess;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.reflect.Field;

/**
 * 字段访问测试
 *
 * @author Sando Geek
 * @since v1.0 2019/4/10
 **/
public class FieldAccessBenchmark {
    private static final UserService target = new UserService();
    private static FieldAccess fieldAccess = FieldAccess.accessUnsafe(UserService.class);
    private static int stateIntegerIndex = fieldAccess.getIndex("stateInteger");
    private static Field stateIntegerField;

    static {
        try {
            stateIntegerField = UserService.class.getField("stateInteger");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        stateIntegerField.setAccessible(true);
    }

    @Test
    void name() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FieldAccessBenchmark.class.getName())
                .warmupIterations(3)
                .measurementIterations(4)
                .forks(3)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void testDirect() throws Exception {
        target.stateInteger = 4388;
    }

    @Benchmark
    public void testASM() throws Exception {
        fieldAccess.setInt(target, stateIntegerIndex, 4388);
    }

    @Benchmark
    public void testJdkReflect() {
        try {
            stateIntegerField.set(target, 1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
