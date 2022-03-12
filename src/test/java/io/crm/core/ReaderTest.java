package io.crm.core;

import io.vertx.core.Future;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {
    @Test
    public void testReader() {
        int ival = new Random().nextInt();
        String res = handleReader(ival, (i, istr) -> Future.succeededFuture(i.toString() + istr));
        assertEquals(String.valueOf(ival) + ival, res);
    }

    private String handleReader(int ival, Reader1<Integer, String, String> reader) {
        return reader.read(ival, String.valueOf(ival)).result();
    }
}