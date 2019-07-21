package com.mmorpg.mbdl.staticresource;

import com.mmorpg.mbdl.business.container.res.ItemRes;
import com.mmorpg.mbdl.framework.common.utils.JsonUtil;
import com.mmorpg.mbdl.framework.resource.core.ProtostuffCodec;
import com.mmorpg.mbdl.framework.resource.exposed.IStaticRes;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 静态资源测试
 *
 * @author Sando Geek
 * @since v1.0 2019/7/21
 **/
@State(Scope.Thread)
public class StaticResTest {
    private IStaticRes<Integer, ItemRes> itemResIStaticRes;
    private transient ProtostuffCodec<IStaticRes<Integer, ItemRes>> codec;
    private byte[] bytes;
    private String string;

    @Setup
    @Test
    public void init() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        itemResIStaticRes = (IStaticRes<Integer, ItemRes>) ctx.getBean("staticResIntegerItemRes");
        codec = new ProtostuffCodec(itemResIStaticRes.getClass());
        bytes = codec.encode(itemResIStaticRes);
        string = JsonUtil.object2String(itemResIStaticRes);
    }

    @Test
    void benchmark() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(StaticResTest.class.getName())
                .warmupIterations(3)
                .measurementIterations(3)
                .forks(2)
                .build();

        new Runner(opt).run();
    }

    @Test
    @Benchmark
    public void protostuff() throws IOException {
        Object decode =  codec.decode(bytes);
    }
    @Test
    @Benchmark
    public void jackson() {
        IStaticRes iStaticRes = JsonUtil.string2Object(string, itemResIStaticRes.getClass());
    }
}
