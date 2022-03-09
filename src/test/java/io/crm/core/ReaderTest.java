package io.crm.core;

import io.vertx.core.Future;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {
    @Test
    public void testReader() {
        int ival = new Random().nextInt();
        String res = handleReader(ival, (i) -> Future.succeededFuture(i.toString()));
        assertEquals(ival, Integer.valueOf(res));
    }

    private String handleReader(int ival, Reader<Integer, String> reader) {
        return reader.apply(ival).result();
    }
}