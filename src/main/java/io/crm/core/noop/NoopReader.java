package io.crm.core.noop;

import io.crm.core.Reader;
import io.vertx.core.Future;

/**
 * Created by xiongxl on 2022/3/16
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class NoopReader<K, V> implements Reader<K, V> {
    private final V expectedValue;
    private static final NoopReader NOOP = new NoopReader();

    public NoopReader() {
        this(null);
    }

    public NoopReader(V expectedValue) {
        this.expectedValue = expectedValue;
    }

    public static <K, V> Reader<K, V> get() {
        return NOOP;
    }

    public static <K, V> Reader<K, V> make(V expectedValue) {
        return new NoopReader(expectedValue);
    }

    @Override
    public Future<V> read(K key) {
        return Future.succeededFuture(expectedValue);
    }
}
