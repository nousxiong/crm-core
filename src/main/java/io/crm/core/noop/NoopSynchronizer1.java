package io.crm.core.noop;

import io.crm.core.Locker;
import io.crm.core.Synchronizer1;
import io.smallrye.mutiny.Uni;

/**
 * Created by xiongxl in 2022/4/9
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class NoopSynchronizer1<K, A> implements Synchronizer1<K, A> {
    private final Locker expectedLocker;
    private static final NoopSynchronizer1 NOOP = new NoopSynchronizer1();

    public NoopSynchronizer1() {
        this(NoopLocker.get());
    }

    public NoopSynchronizer1(Locker expectedLocker) {
        this.expectedLocker = expectedLocker;
    }

    public static <K, A> Synchronizer1<K, A> get() {
        return NOOP;
    }

    public static <K, A> Synchronizer1<K, A> make(Locker expectedLocker) {
        return new NoopSynchronizer1(expectedLocker);
    }

    @Override
    public Uni<Locker> acquire(K key, A arg) {
        return Uni.createFrom().item(expectedLocker);
    }
}
