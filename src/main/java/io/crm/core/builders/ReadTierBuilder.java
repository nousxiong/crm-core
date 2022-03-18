package io.crm.core.builders;

import io.crm.core.Cacher;
import io.crm.core.Interceptor;
import io.crm.core.ReadTier;
import io.crm.core.Reader;

/**
 * Created by xiongxl in 2022/3/12
 */
public class ReadTierBuilder<K, V> extends ReadTier1Builder<K, V, Void> {

    private ReadTierBuilder() {
        super();
    }

    public static <K, V> ReadTierBuilder<K, V> newBuilder(Class<K> keyType, Class<V> valueType) {
        return new ReadTierBuilder<>();
    }

    public ReadTierBuilder<K, V> withReader(Reader<K, V> reader) {
        super.withReader(reader.toReader1());
        return this;
    }

    public ReadTierBuilder<K, V> withCacher(Cacher<K, V> cacher) {
        super.withCacher(cacher.toCacher1());
        return this;
    }

    public ReadTierBuilder<K, V> withInterceptor(Interceptor<K, V> interceptor) {
        super.withInterceptor(interceptor.toInterceptor1());
        return this;
    }

    @Override
    public ReadTier<K, V> build() {
        return new ReadTier<>(
                reader.toReader(),
                cacher.toCacher(),
                interceptor.toInterceptor()
        );
    }
}
