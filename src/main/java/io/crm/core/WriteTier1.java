package io.crm.core;

import io.crm.core.noop.NoopInterceptor1;
import io.crm.core.noop.NoopWriter1;

import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/15
 */
public class WriteTier1<K, V, A> {
    private final Writer1<K, V, A> writer;
    private final Interceptor1<K, V, A> interceptor;

    public Writer1<K, V, A> getWriter() {
        return writer;
    }

    public Interceptor1<K, V, A> getInterceptor() {
        return interceptor;
    }

    public WriteTier1() {
        this(NoopWriter1.get(), NoopInterceptor1.get());
    }

    public WriteTier1(Writer1<K, V, A> writer) {
        this(writer, NoopInterceptor1.get());
    }

    public WriteTier1(Writer1<K, V, A> writer, Interceptor1<K, V, A> interceptor) {
        Objects.requireNonNull(writer);
        Objects.requireNonNull(interceptor);
        this.writer = writer;
        this.interceptor = interceptor;
    }
}
