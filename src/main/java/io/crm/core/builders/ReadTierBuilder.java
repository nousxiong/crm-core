package io.crm.core.builders;

import io.crm.core.Cacher;
import io.crm.core.Interceptor;
import io.crm.core.ReadTier;
import io.crm.core.Reader;

import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/12
 */
public class ReadTierBuilder<K, V> implements Builder<ReadTier<K, V>> {
    private Reader<K, V> reader;
    private Cacher<K, V> cacher;
    private Interceptor<K, V> interceptor;

    private ReadTierBuilder() {
    }

    public static <K, V> ReadTierBuilder<K, V> newBuilder(Class<K> keyType, Class<V> valueType) {
        return new ReadTierBuilder<>();
    }

    public ReadTierBuilder<K, V> withReader(Reader<K, V> reader) {
        Objects.requireNonNull(reader);
        this.reader = reader;
        return this;
    }

    public ReadTierBuilder<K, V> withCacher(Cacher<K, V> cacher) {
        this.cacher = cacher;
        return this;
    }

    public ReadTierBuilder<K, V> withInterceptor(Interceptor<K, V> interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    @Override
    public ReadTier<K, V> build() {
        return new ReadTier<>(reader, cacher, interceptor);
    }
}
