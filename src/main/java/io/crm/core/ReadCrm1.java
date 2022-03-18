package io.crm.core;

import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongxl in 2022/3/9
 * @param <K>
 * @param <V>
 * @param <A>
 */
public class ReadCrm1<K, V, A> {
    private final List<ReadTier1<K, V, A>> readTiers = new ArrayList<>();

    public ReadCrm1() {
    }

    public ReadCrm1(List<ReadTier1<K, V, A>> readTiers) {
        if (readTiers == null) return;
        this.readTiers.addAll(readTiers);
    }

    public Future<V> read(K key, A arg) {
        if (readTiers.isEmpty()) return Future.succeededFuture();
        return read(0, key, arg);
    }

    private Future<V> read(int i, K key, A arg) {
        if (i >= readTiers.size()) return Future.succeededFuture();
        ReadTier1<K, V, A> tier = readTiers.get(i);
        return tier.getReader().read(key, arg).compose(
                v -> v != null ? cache(i - 1, key, v, arg) : read(i + 1, key, arg)
        );
    }

    private Future<V> cache(int i, K key, V value, A arg) {
        if (i < 0) return Future.succeededFuture(value);
        ReadTier1<K, V, A> tier = readTiers.get(i);
        if (!tier.getInterceptor().intercept(key, value, arg)) return cache(i - 1, key, value, arg);
        return tier.getCacher().cache(key, value, arg).compose(v -> cache(i - 1, key, v, arg));
    }
}
