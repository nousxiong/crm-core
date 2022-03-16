package io.crm.core.noop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoopCacherTest {

    @Test
    void get() {
        String value = "dummy";
        assertEquals(value, NoopCacher.get().cache(1, value).result());
    }

    @Test
    void make() {
        String expected = "expected";
        assertEquals(expected, NoopCacher.make(expected).cache(1, "value").result());
        assertEquals(expected, NoopCacher.make(expected).cache(2, "value").result());
        assertNull(NoopCacher.make(null).cache(1, "value").result());
        assertNull(NoopCacher.make(null).cache(2, "value").result());
    }
}