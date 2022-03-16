package io.crm.core.noop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoopCacher1Test {

    @Test
    void get() {
        String value = "dummy";
        assertEquals(value, NoopCacher1.get().cache(1, value, "arg").result());
    }

    @Test
    void make() {
        String expected = "expected";
        assertEquals(expected, NoopCacher1.make(expected).cache(1, "value", "arg").result());
        assertEquals(expected, NoopCacher1.make(expected).cache(2, "value", "arg").result());
        assertNull(NoopCacher1.make(null).cache(1, "value", "arg").result());
        assertNull(NoopCacher1.make(null).cache(2, "value", "arg").result());
    }
}