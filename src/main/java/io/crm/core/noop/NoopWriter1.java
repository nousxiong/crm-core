package io.crm.core.noop;

import io.crm.core.Writer1;
import io.vertx.core.Future;

/**
 * Created by xiongxl on 2022/3/16
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class NoopWriter1<K, V, A> implements Writer1<K, V, A> {
    private final V expectedValue;
    private boolean expected = false;
    private static final NoopWriter1 NOOP = new NoopWriter1();

    public NoopWriter1() {
        expectedValue = null;
    }

    public NoopWriter1(V expectedValue) {
        this.expectedValue = expectedValue;
        this.expected = true;
    }

    public static <K, V, A> Writer1<K, V, A> get() {
        return NOOP;
    }

    public static <K, V, A> Writer1<K, V, A> make(V expectedValue) {
        return new NoopWriter1(expectedValue);
    }

    @Override
    public Future<V> write(K key, V value, A arg) {
        return Future.succeededFuture(expected ? expectedValue : value);
    }
}
