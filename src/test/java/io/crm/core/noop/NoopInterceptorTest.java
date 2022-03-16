package io.crm.core.noop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoopInterceptorTest {

    @Test
    void get() {
        assertTrue(NoopInterceptor.get().intercept(1, "value"));
        assertTrue(NoopInterceptor.get().intercept(2, "value"));
    }

    @Test
    void make() {
        assertFalse(NoopInterceptor.make(false).intercept(1, "value"));
        assertFalse(NoopInterceptor.make(false).intercept(2, "value"));
    }
}