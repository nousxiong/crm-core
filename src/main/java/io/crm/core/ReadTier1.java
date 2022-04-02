package io.crm.core;

/**
 * Created by xiongxl in 2022/3/12
 * 带参数的ReadTier
 * @see ReadTier
 */
public class ReadTier1<K, V, A> {
    private final Reader1<K, V, A> reader;
    private final Cacher1<K, V, A> cacher;
    private final Interceptor1<K, V, A> interceptor;
    private final Synchronizer1<K, A> synchronizer;

    public Reader1<K, V, A> getReader() {
        return reader;
    }

    public Cacher1<K, V, A> getCacher() {
        return cacher;
    }

    public Interceptor1<K, V, A> getInterceptor() {
        return interceptor;
    }

    public Synchronizer1<K, A> getSynchronizer1() {
        return synchronizer;
    }

    public ReadTier1() {
        this(null, null, null);
    }

    public ReadTier1(Reader1<K, V, A> reader) {
        this(reader, null, null);
    }

    public ReadTier1(Reader1<K, V, A> reader, Cacher1<K, V, A> cacher) {
        this(reader, cacher, null);
    }

    public ReadTier1(Reader1<K, V, A> reader, Cacher1<K, V, A> cacher, Interceptor1<K, V, A> interceptor) {
        this(reader, cacher, interceptor, null);
    }

    public ReadTier1(Reader1<K, V, A> reader, Cacher1<K, V, A> cacher, Interceptor1<K, V, A> interceptor, Synchronizer1<K, A> synchronizer) {
        this.reader = reader;
        this.cacher = cacher;
        this.interceptor = interceptor;
        this.synchronizer = synchronizer;
    }
}
