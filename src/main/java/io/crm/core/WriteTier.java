package io.crm.core;

import io.crm.core.noop.NoopArg;

/**
 * Created by xiongxl in 2022/3/15
 */
public class WriteTier<K, V> extends WriteTier1<K, V, NoopArg> {

    public WriteTier() {
        this(null, null);
    }

    public WriteTier(Writer<K, V> writer) {
        this(writer, null);
    }

    public WriteTier(Writer<K, V> writer, Interceptor<K, V> interceptor) {
        super(Writer.toWriter1(writer), Interceptor.toInterceptor1(interceptor));
    }
}
