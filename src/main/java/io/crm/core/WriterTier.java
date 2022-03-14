package io.crm.core;

/**
 * Created by xiongxl in 2022/3/15
 */
public class WriterTier<K, V> {
    private final Writer<K, V> writer;
    private final Interceptor<K, V> interceptor;

    public Writer<K, V> getSaver() {
        return writer;
    }

    public Interceptor<K, V> getInterceptor() {
        return interceptor;
    }

    public WriterTier(Writer<K, V> writer, Interceptor<K, V> interceptor) {
        this.writer = writer;
        this.interceptor = interceptor;
    }
}
