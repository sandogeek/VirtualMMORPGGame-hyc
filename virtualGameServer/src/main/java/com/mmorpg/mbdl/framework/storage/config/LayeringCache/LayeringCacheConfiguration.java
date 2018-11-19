package com.mmorpg.mbdl.framework.storage.config.LayeringCache;

import com.github.xiaolyuh.aspect.LayeringAspect;
import com.github.xiaolyuh.manager.LayeringCacheManager;
import com.github.xiaolyuh.serializer.FastJsonRedisSerializer;
import com.github.xiaolyuh.serializer.StringRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

// @Configuration

/**
 * LayeringCache配置
 * @author Sando Geek
 */
@ImportResource(locations = {"classpath*:applicationContext.xml"})
public class LayeringCacheConfiguration {
    @Bean
    LayeringCacheBeanPostProcessor storageLayringCacheBeanPostProcessor(){
        return new LayeringCacheBeanPostProcessor();
    }
    @Bean
    @SuppressWarnings("unchecked")
    RedisTemplate redisTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class,"com.mmorpg.mbdl.bussiness");
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }
    @Value("${redis.hostName}")
    private String redisHostName;
    @Value("${redis.port}")
    private int redisPort;
    // @Value("${redis.password}")
    // private String redisPassword;
    @Value("${redis.timeout}")
    private int redisTimeout;
    @Bean
    JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.setHostName(redisHostName);
        jedisConnectionFactory.setPort(redisPort);
        // jedisConnectionFactory.setPassword(redisPassword);
        jedisConnectionFactory.setTimeout(redisTimeout);
        return jedisConnectionFactory;
    }
    @Value("${redis.maxIdle}")
    private int redisMaxIdle;
    @Value("${redis.maxTotal}")
    private int redisMaxTotal;
    @Value("${redis.maxWaitMillis}")
    private long redisMaxWaitMillis;
    @Value("${redis.minEvictableIdleTimeMillis}")
    private long redisMinEvictableIdleTimeMillis;
    @Value("${redis.numTestsPerEvictionRun}")
    private int redisNumTestsPerEvictionRun;
    @Value("${redis.timeBetweenEvictionRunsMillis}")
    private long redisTimeBetweenEvictionRunsMillis;
    @Value("${redis.testOnBorrow}")
    private boolean redisTestOnBorrow;
    @Value("${redis.testWhileIdle}")
    private boolean redisTestWhileIdle;
    @Bean
    JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisMaxIdle);
        jedisPoolConfig.setMaxTotal(redisMaxTotal);
        jedisPoolConfig.setMaxWaitMillis(redisMaxWaitMillis);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(redisMinEvictableIdleTimeMillis);
        jedisPoolConfig.setNumTestsPerEvictionRun(redisNumTestsPerEvictionRun);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(redisTimeBetweenEvictionRunsMillis);
        jedisPoolConfig.setTestOnBorrow(redisTestOnBorrow);
        jedisPoolConfig.setTestWhileIdle(redisTestWhileIdle);
        return jedisPoolConfig;
    }
    @Bean
    @Autowired
    LayeringCacheManager layeringCacheManager(RedisTemplate redisTemplate){
        LayeringCacheManager layeringCacheManager = new LayeringCacheManager(redisTemplate);
        // 统计开关
        layeringCacheManager.setStats(true);
        return layeringCacheManager;
    }
    @Bean
    LayeringAspect layeringAspect(){
        return new LayeringAspect();
    }

}
