package io.crm.core.noop;

import io.crm.core.Cacher1;
import io.smallrye.mutiny.Uni;

/**
 * Created by xiongxl on 2022/3/16
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class NoopCacher1<K, V, A> implements Cacher1<K, V, A> {
    private final V expectedValue;
    private boolean expected = false;
    private static final NoopCacher1 NOOP = new NoopCacher1();

    public NoopCacher1() {
        expectedValue = null;
    }

    public NoopCacher1(V expectedValue) {
        this.expectedValue = expectedValue;
        this.expected = true;
    }

    public static <K, V, A> Cacher1<K, V, A> get() {
        return NOOP;
    }

    public static <K, V, A> Cacher1<K, V, A> make(V expectedValue) {
        return new NoopCacher1(expectedValue);
    }

    @Override
    public Uni<V> cache(K key, V value, A arg) {
        return Uni.createFrom().item(expected ? expectedValue : value);
    }
}
