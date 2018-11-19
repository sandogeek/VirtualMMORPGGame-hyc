package com.mmorpg.mbdl.framework.storage.config.JetCache;

import com.alicp.jetcache.CacheBuilder;
import com.alicp.jetcache.anno.CacheConsts;
import com.alicp.jetcache.anno.support.GlobalCacheConfig;
import com.alicp.jetcache.anno.support.SpringConfigProvider;
import com.alicp.jetcache.embedded.CaffeineCacheBuilder;
import com.alicp.jetcache.redis.RedisCacheBuilder;
import com.alicp.jetcache.support.FastjsonKeyConvertor;
import com.alicp.jetcache.support.KryoValueDecoder;
import com.alicp.jetcache.support.KryoValueEncoder;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.Pool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * jetcache配置类
 * <p>如果不能解决缓存一致性的问题，请勿使用注解方式的缓存：<br>
 *     问题：玩家登陆时，需要根据玩家账号查找玩家Id，如果以玩家Id为主键，那么就要使用findByAccount查找Entity,为了加快速度，你用注解
 *     创建缓存。这似乎没什么问题，但是，当使用IStorage更新缓存时，这个注解生成的缓存并不会跟着更新，所以就导致了缓存不一致的问题。
 *     在这个场景中，为了加快速度，我们可以用账号作为主键，也就不需要再创建一个缓存。
 * </p>
 *
 * @author Sando Geek
 * @since v1.0
 **/
// @EnableMethodCache(basePackages = "com.mmorpg.mbdl")
// @EnableCreateCacheAnnotation
public class JetCacheConfiguration {
    @Bean
    JetCacheBeanPostProcessor jetCacheBeanPostProcessor(){
        return  new JetCacheBeanPostProcessor();
    }

    @Bean
    public Pool<Jedis> pool(){
        GenericObjectPoolConfig pc = new GenericObjectPoolConfig();
        pc.setMinIdle(2);
        pc.setMaxIdle(10);
        pc.setMaxTotal(10);
        return new JedisPool(pc, "localhost", 6379);
    }

    @Bean
    public SpringConfigProvider springConfigProvider() {
        return new SpringConfigProvider();
    }

    @Bean
    public GlobalCacheConfig config(SpringConfigProvider configProvider, Pool<Jedis> pool){
        Map<String, CacheBuilder> localBuilders = new HashMap<>(1);
        CacheBuilder localBuilder = CaffeineCacheBuilder.createCaffeineCacheBuilder()
                .expireAfterAccess(2, TimeUnit.MINUTES)
                .keyConvertor(FastjsonKeyConvertor.INSTANCE);
        localBuilders.put(CacheConsts.DEFAULT_AREA, localBuilder);

        Map<String, CacheBuilder> remoteBuilders = new HashMap<>(1);
        RedisCacheBuilder remoteCacheBuilder = RedisCacheBuilder.createRedisCacheBuilder()
                .expireAfterWrite(30,TimeUnit.MINUTES)
                .keyConvertor(FastjsonKeyConvertor.INSTANCE)
                // TODO 使用ProtoStuff编码解码
                .valueEncoder(KryoValueEncoder.INSTANCE)
                .valueDecoder(KryoValueDecoder.INSTANCE)
                .jedisPool(pool);
        remoteBuilders.put(CacheConsts.DEFAULT_AREA, remoteCacheBuilder);

        GlobalCacheConfig globalCacheConfig = new GlobalCacheConfig();
        globalCacheConfig.setHiddenPackages(new String[]{"com.mmorpg.mbdl"});
        globalCacheConfig.setConfigProvider(configProvider);
        globalCacheConfig.setLocalCacheBuilders(localBuilders);
        globalCacheConfig.setRemoteCacheBuilders(remoteBuilders);
        globalCacheConfig.setStatIntervalMinutes(15);
        globalCacheConfig.setAreaInCacheName(false);

        return globalCacheConfig;
    }
}
