package io.crm.core.noop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoopWriter1Test {

    @Test
    void get() {
        String value = "dummy";
        Assertions.assertEquals(value, NoopWriter1.get().write(1, value, "arg").await().indefinitely());
    }

    @Test
    void make() {
        String expected = "expected";
        Assertions.assertEquals(expected, NoopWriter1.make(expected).write(1, "value", "arg").await().indefinitely());
        Assertions.assertEquals(expected, NoopWriter1.make(expected).write(2, "value", "arg").await().indefinitely());
        assertNull(NoopWriter1.make(null).write(1, "value", "arg").await().indefinitely());
        assertNull(NoopWriter1.make(null).write(2, "value", "arg").await().indefinitely());
    }
}