package io.crm.core;

import io.crm.core.noop.NoopArg;
import io.crm.core.noop.NoopInterceptor;
import io.crm.core.noop.NoopWriter;

/**
 * Created by xiongxl in 2022/3/15
 */
public class WriteTier<K, V> extends WriteTier1<K, V, NoopArg> {

    public WriteTier() {
        this(NoopWriter.get(), NoopInterceptor.get());
    }

    public WriteTier(Writer<K, V> writer) {
        this(writer, NoopInterceptor.get());
    }

    public WriteTier(Writer<K, V> writer, Interceptor<K, V> interceptor) {
        super(writer.toWriter1(), interceptor.toInterceptor1());
    }
}
