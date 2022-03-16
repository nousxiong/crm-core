package io.crm.core.builders;

import io.crm.core.Interceptor;
import io.crm.core.Writer;
import io.crm.core.WriteTier;
import io.crm.core.noop.NoopInterceptor;
import io.crm.core.noop.NoopWriter;

import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/15
 */
public class WriteTierBuilder<K, V> implements Builder<WriteTier<K, V>> {
    private Writer<K, V> writer = NoopWriter.get();
    private Interceptor<K, V> interceptor = NoopInterceptor.get();

    private WriteTierBuilder() {
    }

    public static <K, V> WriteTierBuilder<K, V> newBuilder(Class<K> keyType, Class<V> valueType) {
        return new WriteTierBuilder<>();
    }

    public WriteTierBuilder<K, V> withWriter(Writer<K, V> writer) {
        Objects.requireNonNull(writer);
        this.writer = writer;
        return this;
    }

    public WriteTierBuilder<K, V> withInterceptor(Interceptor<K, V> interceptor) {
        Objects.requireNonNull(interceptor);
        this.interceptor = interceptor;
        return this;
    }

    @Override
    public WriteTier<K, V> build() {
        return new WriteTier<>(writer, interceptor);
    }
}
