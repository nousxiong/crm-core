package io.crm.core.noop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoopWriterTest {

    @Test
    void get() {
        String value = "dummy";
        assertEquals(value, NoopWriter.get().write(1, value).result());
    }

    @Test
    void make() {
        String expected = "expected";
        assertEquals(expected, NoopWriter.make(expected).write(1, "value").result());
        assertEquals(expected, NoopWriter.make(expected).write(2, "value").result());
        assertNull(NoopWriter.make(null).write(1, "value").result());
        assertNull(NoopWriter.make(null).write(2, "value").result());
    }
}