package io.crm.core;

/**
 * Created by xiongxl in 2022/3/15
 */
public class WriteTier1<K, V, A> {
    private final Writer1<K, V, A> writer;
    private final Interceptor1<K, V, A> interceptor;
    private final Synchronizer1<K, A> synchronizer;

    public Writer1<K, V, A> getWriter() {
        return writer;
    }

    public Interceptor1<K, V, A> getInterceptor() {
        return interceptor;
    }

    public Synchronizer1<K, A> getSynchronizer1() {
        return synchronizer;
    }

    public WriteTier1() {
        this(null, null);
    }

    public WriteTier1(Writer1<K, V, A> writer) {
        this(writer, null);
    }

    public WriteTier1(Writer1<K, V, A> writer, Interceptor1<K, V, A> interceptor) {
        this(writer, interceptor, null);
    }

    public WriteTier1(
            Writer1<K, V, A> writer,
            Interceptor1<K, V, A> interceptor,
            Synchronizer1<K, A> synchronizer
    ) {
        this.writer = writer;
        this.interceptor = interceptor;
        this.synchronizer = synchronizer;
    }
}
