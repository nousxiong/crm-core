package io.crm.core.noop;

import io.crm.core.Locker;
import io.crm.core.Synchronizer;
import io.smallrye.mutiny.Uni;

/**
 * Created by xiongxl in 2022/4/9
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class NoopSynchronizer<K> implements Synchronizer<K> {
    private final Locker expectedLocker;
    private static final NoopSynchronizer NOOP = new NoopSynchronizer();

    public NoopSynchronizer() {
        this(NoopLocker.get());
    }

    public NoopSynchronizer(Locker expectedLocker) {
        this.expectedLocker = expectedLocker;
    }

    public static <K> Synchronizer<K> get() {
        return NOOP;
    }

    public static <K> Synchronizer<K> make(Locker expectedLocker) {
        return new NoopSynchronizer(expectedLocker);
    }

    @Override
    public Uni<Locker> acquire(K key) {
        return Uni.createFrom().item(expectedLocker);
    }
}
