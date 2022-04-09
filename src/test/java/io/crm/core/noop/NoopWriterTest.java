package io.crm.core.noop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoopWriterTest {

    @Test
    void get() {
        String value = "dummy";
        Assertions.assertEquals(value, NoopWriter.get().write(1, value).await().indefinitely());
    }

    @Test
    void make() {
        String expected = "expected";
        Assertions.assertEquals(expected, NoopWriter.make(expected).write(1, "value").await().indefinitely());
        Assertions.assertEquals(expected, NoopWriter.make(expected).write(2, "value").await().indefinitely());
        assertNull(NoopWriter.make(null).write(1, "value").await().indefinitely());
        assertNull(NoopWriter.make(null).write(2, "value").await().indefinitely());
    }
}