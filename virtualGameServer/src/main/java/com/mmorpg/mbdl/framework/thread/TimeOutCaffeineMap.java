package com.mmorpg.mbdl.framework.thread;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 利用缓存Caffeine实现的值带过期时间的Map
 * @author sando
 * @param <K> 键
 * @param <V> 值
 */
public class TimeOutCaffeineMap<K,V> implements ITimeOutHashMap<K,V> {
    private final LoadingCache<K, V> loadingCache;

    public TimeOutCaffeineMap(long timeout, TimeUnit timeUnit, Supplier<? extends V> supplier) {
        // 超过一定时间不写入就会清除缓存
        this.loadingCache = Caffeine.newBuilder().expireAfterWrite(timeout, timeUnit).build(key -> supplier.get());
    }

    @Override
    public V getOrCreate(K key) {
        return loadingCache.get(key);
    }
}
