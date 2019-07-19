package com.mmorpg.mbdl.framework.common.generator;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
public class IdGeneratorTest {
    private static IdGenerator generator = new IdGenerator(1L, 1L, 1L, System.currentTimeMillis());

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(IdGenerator.class.getName())
                .warmupIterations(3)
                .measurementIterations(3)
                .forks(3)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void idGenerate() {
        generator.generate();
    }
}