package io.crm.core;

import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongxl in 2022/3/9
 * 缓存关系读取类
 * @param <K>
 * @param <V>
 */
public class ReadCrm<K, V> {
    private final List<ReadTier<K, V>> readTiers = new ArrayList<>();

    public ReadCrm() {
    }

    public ReadCrm(List<ReadTier<K, V>> readTiers) {
        if (readTiers == null) return;
        this.readTiers.addAll(readTiers);
    }

    /**
     * 读取操作
     * @param key 键
     * @return Future值
     */
    public Future<V> read(K key) {
        if (readTiers.isEmpty()) return Future.succeededFuture();
        return read(0, key);
    }

    private Future<V> read(int i, K key) {
        if (i >= readTiers.size()) return Future.succeededFuture();
        ReadTier<K, V> tier = readTiers.get(i);
        return tier.getReader().read(key).compose(v -> v != null ? cache(i - 1, key, v) : read(i + 1, key));
    }

    private Future<V> cache(int i, K key, V value) {
        if (i < 0) return Future.succeededFuture(value);
        ReadTier<K, V> tier = readTiers.get(i);
        if (tier.getCacher() == null) return cache(i - 1, key, value);
        if (!tier.getInterceptor().intercept(key, value)) return cache(i - 1, key, value);
        return tier.getCacher().cache(key, value).compose(v -> cache(i - 1, key, v));
    }
}
