package io.crm.core.noop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NoopInterceptorTest {

    @Test
    void get() {
        Assertions.assertTrue(NoopInterceptor.get().intercept(1, "value"));
        Assertions.assertTrue(NoopInterceptor.get().intercept(2, "value"));
    }

    @Test
    void make() {
        Assertions.assertFalse(NoopInterceptor.make(false).intercept(1, "value"));
        Assertions.assertFalse(NoopInterceptor.make(false).intercept(2, "value"));
    }
}