package io.crm.core.noop;

import io.crm.core.Locker;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoopSynchronizer1Test {

    @Test
    void get() {
        assertEquals(NoopLocker.get(), NoopSynchronizer1.get().acquire("key", "arg").await().indefinitely());
    }

    private static class MyLocker implements Locker {
        public MyLocker() {
        }

        @Override
        public Uni<Void> release() {
            return Uni.createFrom().nullItem();
        }
    }

    @Test
    void make() {
        Locker myLocker = new MyLocker();
        assertEquals(myLocker, NoopSynchronizer1.make(myLocker).acquire("key1", "arg1").await().indefinitely());
        assertEquals(myLocker, NoopSynchronizer1.make(myLocker).acquire("key2", "arg2").await().indefinitely());
    }
}