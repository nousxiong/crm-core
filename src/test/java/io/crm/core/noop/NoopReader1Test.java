package io.crm.core.noop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoopReader1Test {

    @Test
    void get() {
        assertNull(NoopReader1.get().read(1, "arg").await().indefinitely());
    }

    @Test
    void make() {
        String value = "myValue";
        assertEquals(value, NoopReader1.make(value).read(1, "arg").await().indefinitely());
        assertEquals(value, NoopReader1.make(value).read(2, "arg").await().indefinitely());
    }
}