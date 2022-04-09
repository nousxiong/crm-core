package io.crm.core.noop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NoopInterceptor1Test {

    @Test
    void get() {
        Assertions.assertTrue(NoopInterceptor1.get().intercept(1, "value", "arg"));
        Assertions.assertTrue(NoopInterceptor1.get().intercept(2, "value", "arg"));
    }

    @Test
    void make() {
        Assertions.assertFalse(NoopInterceptor1.make(false).intercept(1, "value", "arg"));
        Assertions.assertFalse(NoopInterceptor1.make(false).intercept(2, "value", "arg"));
    }
}