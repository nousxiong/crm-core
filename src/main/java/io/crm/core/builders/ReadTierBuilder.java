package io.crm.core.builders;

import io.crm.core.*;
import io.crm.core.noop.*;

import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/12
 */
public class ReadTierBuilder<K, V> implements Builder<ReadTier<K, V>> {
    private Reader<K, V> reader = NoopReader.get();
    private Cacher<K, V> cacher = NoopCacher.get();
    private Interceptor<K, V> interceptor = NoopInterceptor.get();

    private ReadTierBuilder() {
        super();
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
        Objects.requireNonNull(cacher);
        this.cacher = cacher;
        return this;
    }

    public ReadTierBuilder<K, V> withInterceptor(Interceptor<K, V> interceptor) {
        Objects.requireNonNull(interceptor);
        this.interceptor = interceptor;
        return this;
    }

    @Override
    public ReadTier<K, V> build() {
        return new ReadTier<>(reader, cacher, interceptor);
    }
}
