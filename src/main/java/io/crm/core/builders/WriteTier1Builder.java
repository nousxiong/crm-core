package io.crm.core.builders;

import io.crm.core.Interceptor1;
import io.crm.core.WriteTier1;
import io.crm.core.Writer1;
import io.crm.core.noop.NoopInterceptor1;
import io.crm.core.noop.NoopWriter1;

import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/15
 */
public class WriteTier1Builder<K, V, A> implements Builder<WriteTier1<K, V, A>> {
    private Writer1<K, V, A> writer = NoopWriter1.get();
    private Interceptor1<K, V, A> interceptor = NoopInterceptor1.get();

    private WriteTier1Builder() {
    }

    public static <K, V, A> WriteTier1Builder<K, V, A> newBuilder(Class<K> keyType, Class<V> valueType, Class<A> argType) {
        return new WriteTier1Builder<>();
    }

    public WriteTier1Builder<K, V, A> withWriter(Writer1<K, V, A> writer) {
        Objects.requireNonNull(writer);
        this.writer = writer;
        return this;
    }

    public WriteTier1Builder<K, V, A> withInterceptor(Interceptor1<K, V, A> interceptor) {
        Objects.requireNonNull(interceptor);
        this.interceptor = interceptor;
        return this;
    }

    @Override
    public WriteTier1<K, V, A> build() {
        return new WriteTier1<>(writer, interceptor);
    }
}
