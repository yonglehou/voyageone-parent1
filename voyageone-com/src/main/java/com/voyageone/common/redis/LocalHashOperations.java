package com.voyageone.common.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ScanOptions;

import java.util.*;
import java.util.Map.Entry;

/**
 * LocalHashOperations
 *
 * @author aooer 2016/4/5.
 * @version 2.0.0
 * @since 2.0.0
 */
public class LocalHashOperations<K, HK, HV> implements HashOperations<K, HK, HV> {

    /* 本地RedisTemplate 缓存*/
    private Map<K, Map<HK, HV>> localCache = CacheContainer.getInstance().getLocalCache();


    public void delete(K key) {
        localCache.remove(key);
    }

    @Override
    public void delete(K key, Object... hashKeys) {
        if (localCache.get(key) != null) {
            Arrays.asList(hashKeys).forEach(localCache.get(key)::remove);
        }
    }

    @Override
    public Boolean hasKey(K key, Object hashKey) {
        return localCache.get(key) != null && localCache.get(key).containsKey(hashKey);
    }

    public Boolean hasKey(K key) {
        return localCache.containsKey(key);
    }

    @Override
    public HV get(K key, Object hashKey) {
        Map<HK, HV> map = getCache(key);
        if (map == null) return null;
        return map.get(hashKey);
    }

    @Override
    public List<HV> multiGet(K key, Collection<HK> hashKeys) {
        List<HV> hvs = Lists.newCopyOnWriteArrayList();
        if (localCache.get(key) != null) {
            hashKeys.forEach(k -> hvs.add(localCache.get(key).get(k)));
        }
        return hvs;
    }

    @Override
    public Set<HK> keys(K key) {
        if (localCache.get(key) != null) {
            return localCache.get(key).keySet();
        }
        return new HashSet<>();
    }

    @Override
    public Long size(K key) {
        if (localCache.get(key) != null) {
            return Integer.toUnsignedLong(localCache.get(key).size());
        } else {
            return 0L;
        }
    }

    @Override
    public void putAll(K key, Map<? extends HK, ? extends HV> m) {
        localCache.put(key, (Map<HK, HV>) m);
    }

    @Override
    public void put(K key, HK hashKey, HV value) {
        Map<HK, HV> map = getCache(key);
        map.put(hashKey, value);
    }

    private Map<HK, HV> getCache(K key) {
        Map<HK, HV> map = localCache.get(key);
        if (map == null) {
            createCache(key);
            map = localCache.get(key);
        }
        return map;
    }

    private synchronized void createCache(K key) {
        if (!localCache.containsKey(key)) {
            Map<HK, HV> map = new HashMap<>();
            localCache.put(key, map);
        }
    }

    @Override
    public Boolean putIfAbsent(K key, HK hashKey, HV value) {
        if (localCache.get(key) != null && localCache.get(key).containsKey(hashKey)) {
            return false;
        } else {
            put(key, hashKey, value);
            return true;
        }
    }

    @Override
    public List<HV> values(K key) {
        List<HV> values = Lists.newCopyOnWriteArrayList();
        if (localCache.get(key) != null) {
            values.addAll(localCache.get(key).values());
        }
        return values;
    }

    @Override
    public Map<HK, HV> entries(K key) {
        return localCache.get(key);
    }

    @Override
    public RedisOperations<K, ?> getOperations() {
        return null;
    }

    @Override
    public Cursor<Entry<HK, HV>> scan(K key, ScanOptions options) {
        return null;
    }

    @Override
    public Long increment(K key, HK hashKey, long delta) {
        return null;
    }

    @Override
    public Double increment(K key, HK hashKey, double delta) {
        return null;
    }


    static class CacheContainer<K, HK, HV> {

        private Map<K, Map<HK, HV>> localCache = Maps.newConcurrentMap();

        public Map<K, Map<HK, HV>> getLocalCache() {
            return localCache;
        }

        private static class CacheHolder {
            private static CacheContainer box = new CacheContainer();
        }

        public static CacheContainer getInstance() {
            return CacheHolder.box;
        }
    }

}

