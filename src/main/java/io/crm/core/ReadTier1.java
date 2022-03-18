package io.crm.core;

import io.crm.core.noop.*;

import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/12
 * 带参数的ReadTier
 * @see ReadTier
 */
public class ReadTier1<K, V, A> {
    private final Reader1<K, V, A> reader;
    private final Cacher1<K, V, A> cacher;
    private final Interceptor1<K, V, A> interceptor;

    public Reader1<K, V, A> getReader() {
        return reader;
    }

    public Cacher1<K, V, A> getCacher() {
        return cacher;
    }

    public Interceptor1<K, V, A> getInterceptor() {
        return interceptor;
    }

    public ReadTier1() {
        this(NoopReader1.get(), NoopCacher1.get(), NoopInterceptor1.get());
    }

    public ReadTier1(Reader1<K, V, A> reader) {
        this(reader, NoopCacher1.get(), NoopInterceptor1.get());
    }

    public ReadTier1(Reader1<K, V, A> reader, Cacher1<K, V, A> cacher) {
        this(reader, cacher, NoopInterceptor1.get());
    }

    public ReadTier1(Reader1<K, V, A> reader, Cacher1<K, V, A> cacher, Interceptor1<K, V, A> interceptor) {
        Objects.requireNonNull(reader);
        Objects.requireNonNull(cacher);
        Objects.requireNonNull(interceptor);
        this.reader = reader;
        this.cacher = cacher;
        this.interceptor = interceptor;
    }
}
