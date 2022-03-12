package io.crm.core;

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

    public ReadTier(Reader<K, V> reader, Cacher<K, V> cacher, Interceptor<K, V> interceptor) {
        Objects.requireNonNull(reader);
        this.reader = reader;
        this.cacher = cacher;
        this.interceptor = interceptor;
    }
}
