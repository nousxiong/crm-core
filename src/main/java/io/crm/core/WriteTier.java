package io.crm.core;

import io.crm.core.noop.NoopInterceptor;
import io.crm.core.noop.NoopWriter;

import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/15
 */
public class WriteTier<K, V> {
    private final Writer<K, V> writer;
    private final Interceptor<K, V> interceptor;

    public Writer<K, V> getWriter() {
        return writer;
    }

    public Interceptor<K, V> getInterceptor() {
        return interceptor;
    }

    public WriteTier() {
        this(NoopWriter.get(), NoopInterceptor.get());
    }

    public WriteTier(Writer<K, V> writer) {
        this(writer, NoopInterceptor.get());
    }

    public WriteTier(Writer<K, V> writer, Interceptor<K, V> interceptor) {
        Objects.requireNonNull(writer);
        Objects.requireNonNull(interceptor);
        this.writer = writer;
        this.interceptor = interceptor;
    }
}
