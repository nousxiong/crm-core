package io.crm.core.noop;

import io.crm.core.Reader1;
import io.vertx.core.Future;

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
    public Future<V> read(K key, A arg) {
        return Future.succeededFuture(expectedValue);
    }
}
