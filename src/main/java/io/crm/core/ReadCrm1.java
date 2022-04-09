package io.crm.core;

import io.smallrye.mutiny.Uni;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        if (readTiers == null) {
            return;
        }
        for (ReadTier1<K, V, A> readTier : readTiers) {
            Objects.requireNonNull(readTier);
        }
        this.readTiers.addAll(readTiers);
    }

    public Uni<V> read(K key, A arg) {
        if (readTiers.isEmpty()) {
            return Uni.createFrom().nullItem();
        }
        return read(0, key, arg);
    }

    private Uni<V> read(int i, K key, A arg) {
        if (i >= readTiers.size()) {
            return Uni.createFrom().nullItem();
        }

        ReadTier1<K, V, A> tier = readTiers.get(i);
        Reader1<K, V, A> reader = tier.getReader();
        Synchronizer1<K, A> synchronizer = tier.getSynchronizer1();

        if (synchronizer == null) {
            if (reader == null) {
                return read(i + 1, key, arg);
            } else {
                return reader.read(key, arg)
                        .onItem().ifNotNull().transformToUni(v -> cache(i - 1, key, v, arg))
                        .onItem().ifNull().switchTo(() -> read(i + 1, key, arg));
            }
        } else {
            return synchronizer.acquire(key, arg)
                    .onItem().transformToUni(lk -> {
                        if (reader == null) {
                            return read(i + 1, key, arg)
                                    .onItem().call(lk::release);
                        } else {
                            return reader.read(key, arg)
                                    .onItem().ifNotNull().transformToUni(v -> cache(i - 1, key, v, arg))
                                    .onItem().ifNull().switchTo(() -> read(i + 1, key, arg))
                                    .onItem().call(lk::release);
                        }
                    });
        }
    }

    private Uni<V> cache(int i, K key, V value, A arg) {
        if (i < 0) {
            return Uni.createFrom().item(value);
        }

        ReadTier1<K, V, A> tier = readTiers.get(i);
        Interceptor1<K, V, A> interceptor = tier.getInterceptor();

        if (interceptor != null && !interceptor.intercept(key, value, arg)) {
            return cache(i - 1, key, value, arg);
        }

        Cacher1<K, V, A> cacher = tier.getCacher();
        if (cacher == null) {
            return cache(i - 1, key, value, arg);
        }
        return cacher.cache(key, value, arg)
                .onItem().transformToUni(v -> cache(i - 1, key, v, arg));
    }
}
