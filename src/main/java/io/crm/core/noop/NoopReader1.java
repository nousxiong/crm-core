package io.crm.core.noop;

import io.crm.core.Reader1;
import io.smallrye.mutiny.Uni;

/**
 * Created by xiongxl on 2022/3/16
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class NoopReader1<K, V, A> implements Reader1<K, V, A> {
    private final V expectedValue;
    private static final NoopReader1 NOOP = new NoopReader1();

    public NoopReader1() {
        this(null);
    }

    public NoopReader1(V expectedValue) {
        this.expectedValue = expectedValue;
    }

    public static <K, V, A> Reader1<K, V, A> get() {
        return NOOP;
    }

    public static <K, V, A> Reader1<K, V, A> make(V expectedValue) {
        return new NoopReader1(expectedValue);
    }

    @Override
    public Uni<V> read(K key, A arg) {
        return Uni.createFrom().item(expectedValue);
    }
}
