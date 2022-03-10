package io.crm.core;

import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/9
 * @param <K>
 * @param <V>
 */
public class ReadCrm<K, V> {
    private final List<ReadTier<K, V>> readTiers = new ArrayList<>();

    public ReadCrm(List<ReadTier<K, V>> readTiers) {
        Objects.requireNonNull(readTiers);
        this.readTiers.addAll(readTiers);
    }

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
        return tier.getCacher().cache(key, value).compose(v -> cache(i - 1, key, v));
    }
}
