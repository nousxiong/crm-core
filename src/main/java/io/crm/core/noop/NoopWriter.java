package io.crm.core.noop;

import io.crm.core.Writer;
import io.smallrye.mutiny.Uni;

/**
 * Created by xiongxl on 2022/3/16
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class NoopWriter<K, V> implements Writer<K, V> {
    private final V expectedValue;
    private boolean expected = false;
    private static final NoopWriter NOOP = new NoopWriter();

    public NoopWriter() {
        expectedValue = null;
    }

    public NoopWriter(V expectedValue) {
        this.expectedValue = expectedValue;
        this.expected = true;
    }

    public static <K, V> Writer<K, V> get() {
        return NOOP;
    }

    public static <K, V> Writer<K, V> make(V expectedValue) {
        return new NoopWriter(expectedValue);
    }

    @Override
    public Uni<V> write(K key, V value) {
        return Uni.createFrom().item(expected ? expectedValue : value);
    }
}
