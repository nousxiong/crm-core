package io.crm.core;

import io.crm.core.noop.NoopArg;
import io.crm.core.noop.NoopCacher;
import io.crm.core.noop.NoopInterceptor;
import io.crm.core.noop.NoopReader;

/**
 * Created by xiongxl in 2022/3/9
 */
public class ReadTier<K, V> extends ReadTier1<K, V, NoopArg> {

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
        super(reader.toReader1(), cacher.toCacher1(), interceptor.toInterceptor1());
    }
}
