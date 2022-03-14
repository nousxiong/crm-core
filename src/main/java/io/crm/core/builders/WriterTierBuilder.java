package io.crm.core.builders;

import io.crm.core.Interceptor;
import io.crm.core.Writer;
import io.crm.core.WriterTier;

import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/15
 */
public class WriterTierBuilder<K, V> implements Builder<WriterTier<K, V>> {
    private Writer<K, V> writer;
    private Interceptor<K, V> interceptor;

    private WriterTierBuilder() {
    }

    public static <K, V> WriterTierBuilder<K, V> newBuilder(Class<K> keyType, Class<V> valueType) {
        return new WriterTierBuilder<>();
    }

    public WriterTierBuilder<K, V> withWriter(Writer<K, V> writer) {
        Objects.requireNonNull(writer);
        this.writer = writer;
        return this;
    }

    public WriterTierBuilder<K, V> withInterceptor(Interceptor<K, V> interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    @Override
    public WriterTier<K, V> build() {
        return new WriterTier<>(writer, interceptor);
    }
}
