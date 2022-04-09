package io.crm.core.noop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NoopReaderTest {

    @Test
    void get() {
        assertNull(NoopReader.get().read(1).await().indefinitely());
    }

    @Test
    void make() {
        String value = "myValue";
        assertEquals(value, NoopReader.make(value).read(1).await().indefinitely());
        assertEquals(value, NoopReader.make(value).read(2).await().indefinitely());
    }
}