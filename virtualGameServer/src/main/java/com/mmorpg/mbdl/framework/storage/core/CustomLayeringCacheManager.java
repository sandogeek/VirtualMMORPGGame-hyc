package com.mmorpg.mbdl.framework.storage.core;

import com.github.xiaolyuh.manager.LayeringCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;

/**
 * 由于StorageMySql不是一个Bean但却要获取LayeringCacheManager这个bean，所以拓展出getInstance方法获取bean
 *
 * @author Sando Geek
 * @since v1.0
 **/
public class CustomLayeringCacheManager extends LayeringCacheManager {
    public CustomLayeringCacheManager(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }
    private static CustomLayeringCacheManager self;
    @PostConstruct
    private void init(){
        self=this;
    }
    public static CustomLayeringCacheManager getInstance(){
        return self;
    }
}
