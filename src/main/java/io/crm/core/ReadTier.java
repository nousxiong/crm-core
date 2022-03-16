package io.crm.core;

import io.crm.core.noop.NoopCacher;
import io.crm.core.noop.NoopInterceptor;
import io.crm.core.noop.NoopReader;

import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/9
 */
public class ReadTier<K, V> {
    private final Reader<K, V> reader;
    private final Cacher<K, V> cacher;
    private final Interceptor<K, V> interceptor;

    public Reader<K, V> getReader() {
        return reader;
    }

    public Cacher<K, V> getCacher() {
        return cacher;
    }

    public Interceptor<K, V> getInterceptor() {
        return interceptor;
    }

    public ReadTier() {
        this(NoopReader.get(), NoopCacher.get(), NoopInterceptor.get());
    }

    public ReadTier(Reader<K, V> reader) {
        this(reader, NoopCacher.get(), NoopInterceptor.get());
    }

    public ReadTier(Reader<K, V> reader, Cacher<K, V> cacher) {
        this(reader, cacher, NoopInterceptor.get());
    }

    public ReadTier(Reader<K, V> reader, Cacher<K, V> cacher, Interceptor<K, V> interceptor) {
        Objects.requireNonNull(reader);
        Objects.requireNonNull(cacher);
        Objects.requireNonNull(interceptor);
        this.reader = reader;
        this.cacher = cacher;
        this.interceptor = interceptor;
    }
}
