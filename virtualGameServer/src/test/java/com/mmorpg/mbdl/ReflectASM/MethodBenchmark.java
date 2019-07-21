package com.mmorpg.mbdl.ReflectASM;

import com.mmorpg.mbdl.framework.reflectasm.withunsafe.MethodAccess;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.reflect.Method;

/**
 * 方法调用测试
 *
 * @author Sando Geek
 * @since v1.0 2019/4/10
 **/
public class MethodBenchmark {
    private static final BenchmarkTest BENCHMARK_TEST = new BenchmarkTest();
    private static final UserService target = new UserService();
    private static Method method;

    static {
        try {
            method = target.getClass().getMethod("update", int.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static MethodAccess access = MethodAccess.access(UserService.class);
    private static int index = access.getIndex("update", int.class, String.class);

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MethodBenchmark.class.getName())
                .warmupIterations(3)
                .measurementIterations(5)
                .forks(3)
                .build();

        new Runner(opt).run();
    }

    /**
     * 直接调用
     *
     * @throws Exception
     */
    @Benchmark
    public void testDirect() throws Exception {
        target.update(1, "sandogeek");
    }

    /**
     * JDK反射调用方法
     *
     * @throws Exception
     */
    @Benchmark
    public void testJdkReflect() throws Exception {
        method.invoke(target, 1, "sandogeek");
    }

    @Benchmark
    public void testReflectAsm4Name() {
        access.invoke(target, "update", 1, "sandogeek");
    }

    @Benchmark
    public void testReflectAsm4Index() {
        access.invoke(target, index, 1, "sandogeek");
    }
}
