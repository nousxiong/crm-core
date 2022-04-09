package io.crm.core.noop;

import io.crm.core.Locker;
import io.smallrye.mutiny.Uni;

/**
 * Created by xiongxl in 2022/4/9
 */
public class NoopLocker implements Locker {
    private static final NoopLocker NOOP = new NoopLocker();

    public NoopLocker() {
    }

    public static Locker get() {
        return NOOP;
    }

    public static Locker make() {
        return new NoopLocker();
    }

    @Override
    public Uni<Void> release() {
        return Uni.createFrom().voidItem();
    }
}
