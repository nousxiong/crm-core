package io.crm.core.noop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoopWriter1Test {

    @Test
    void get() {
        String value = "dummy";
        assertEquals(value, NoopWriter1.get().write(1, value, "arg").result());
    }

    @Test
    void make() {
        String expected = "expected";
        assertEquals(expected, NoopWriter1.make(expected).write(1, "value", "arg").result());
        assertEquals(expected, NoopWriter1.make(expected).write(2, "value", "arg").result());
        assertNull(NoopWriter1.make(null).write(1, "value", "arg").result());
        assertNull(NoopWriter1.make(null).write(2, "value", "arg").result());
    }
}