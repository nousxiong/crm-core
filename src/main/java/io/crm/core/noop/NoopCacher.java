package io.crm.core.noop;

import io.crm.core.Cacher;
import io.vertx.core.Future;

/**
 * Created by xiongxl on 2022/3/16
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class NoopCacher<K, V> implements Cacher<K, V> {
    private final V expectedValue;
    private boolean expected = false;
    private static final NoopCacher NOOP = new NoopCacher();

    public NoopCacher() {
        expectedValue = null;
    }

    public NoopCacher(V expectedValue) {
        this.expectedValue = expectedValue;
        this.expected = true;
    }

    public static <K, V> Cacher<K, V> get() {
        return NOOP;
    }

    public static <K, V> Cacher<K, V> make(V expectedValue) {
        return new NoopCacher(expectedValue);
    }

    @Override
    public Future<V> cache(K key, V value) {
        return Future.succeededFuture(expected ? expectedValue : value);
    }
}
