package io.crm.core.noop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoopInterceptor1Test {

    @Test
    void get() {
        assertTrue(NoopInterceptor1.get().intercept(1, "value", "arg"));
        assertTrue(NoopInterceptor1.get().intercept(2, "value", "arg"));
    }

    @Test
    void make() {
        assertFalse(NoopInterceptor1.make(false).intercept(1, "value", "arg"));
        assertFalse(NoopInterceptor1.make(false).intercept(2, "value", "arg"));
    }
}