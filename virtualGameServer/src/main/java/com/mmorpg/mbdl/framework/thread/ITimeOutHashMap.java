package com.mmorpg.mbdl.framework.thread;

/**
 * 依据时间缓存的map
 */
public interface ITimeOutHashMap<K, V> {
    /**
     * 根据键来获取值
     * @param key 键
     * @return 值
     */
    V getOrCreate(K key);
}
