package io.crm.core.noop;

import io.crm.core.Locker;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoopSynchronizerTest {

    @Test
    void get() {
        assertEquals(NoopLocker.get(), NoopSynchronizer.get().acquire("key").await().indefinitely());
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
        assertEquals(myLocker, NoopSynchronizer.make(myLocker).acquire("key1").await().indefinitely());
        assertEquals(myLocker, NoopSynchronizer.make(myLocker).acquire("key2").await().indefinitely());
    }
}