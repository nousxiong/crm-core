package io.crm.core.builders;

import io.crm.core.Interceptor;
import io.crm.core.Synchronizer;
import io.crm.core.WriteTier;
import io.crm.core.Writer;
import io.crm.core.noop.NoopInterceptor;
import io.crm.core.noop.NoopWriter;

/**
 * Created by xiongxl in 2022/3/15
 */
public class WriteTierBuilder<K, V> implements Builder<WriteTier<K, V>> {
    private Writer<K, V> writer = NoopWriter.get();
    private Interceptor<K, V> interceptor = NoopInterceptor.get();
    private Synchronizer<K> synchronizer;

    private WriteTierBuilder() {
    }

    public static <K, V> WriteTierBuilder<K, V> newBuilder(Class<K> keyType, Class<V> valueType) {
        return new WriteTierBuilder<>();
    }

    public WriteTierBuilder<K, V> withWriter(Writer<K, V> writer) {
        this.writer = writer;
        return this;
    }

    public WriteTierBuilder<K, V> withInterceptor(Interceptor<K, V> interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    public WriteTierBuilder<K, V> withSynchronizer(Synchronizer<K> synchronizer) {
        this.synchronizer = synchronizer;
        return this;
    }

    @Override
    public WriteTier<K, V> build() {
        return new WriteTier<>(writer, interceptor, synchronizer);
    }
}
