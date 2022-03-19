package io.crm.core;

import io.crm.core.noop.NoopArg;

/**
 * Created by xiongxl in 2022/3/9
 */
public class ReadTier<K, V> extends ReadTier1<K, V, NoopArg> {

    public ReadTier() {
        this(null, null, null);
    }

    public ReadTier(Reader<K, V> reader) {
        this(reader, null, null);
    }

    public ReadTier(Reader<K, V> reader, Cacher<K, V> cacher) {
        this(reader, cacher, null);
    }

    public ReadTier(Reader<K, V> reader, Cacher<K, V> cacher, Interceptor<K, V> interceptor) {
        super(Reader.toReader1(reader), Cacher.toCacher1(cacher), Interceptor.toInterceptor1(interceptor));
    }
}
