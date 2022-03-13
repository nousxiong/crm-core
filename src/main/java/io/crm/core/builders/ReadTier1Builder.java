package io.crm.core.builders;

import io.crm.core.*;

import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/12
 */
public class ReadTier1Builder<K, V, A> implements Builder<ReadTier1<K, V, A>> {
    private Reader1<K, V, A> reader;
    private Cacher1<K, V, A> cacher;
    private Interceptor1<K, V, A> interceptor;

    private ReadTier1Builder() {
    }

    public static <K, V, A> ReadTier1Builder<K, V, A> newBuilder() {
        return new ReadTier1Builder<>();
    }

    public ReadTier1Builder<K, V, A> withReader(Reader1<K, V, A> reader) {
        Objects.requireNonNull(reader);
        this.reader = reader;
        return this;
    }

    public ReadTier1Builder<K, V, A> withCacher(Cacher1<K, V, A> cacher) {
        this.cacher = cacher;
        return this;
    }

    public ReadTier1Builder<K, V, A> withInterceptor(Interceptor1<K, V, A> interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    @Override
    public ReadTier1<K, V, A> build() {
        return new ReadTier1<>(reader, cacher, interceptor);
    }
}
