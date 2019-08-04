package com.mmorpg.mbdl.framework.storage;

import com.mmorpg.mbdl.TestWithSpring;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

/**
 * Redisson测试
 *
 * @author Sando Geek
 * @since v1.0 2019/8/5
 **/
public class RedissonTest extends TestWithSpring {
    @Autowired
    private RedissonClient client;

    @Test
    void 测试() {
        RMap<String, String> containerEntity = client.getMap("ContainerEntity");
        // containerEntity.fastPut("kkk", "ggg");
        // containerEntity.fastPut("you", "badbad");
        Set<Map.Entry<String, String>> entries = containerEntity.readAllEntrySet();
        System.out.println(1);
    }
}
