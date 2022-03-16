package io.crm.core.noop;

import io.crm.core.Interceptor1;

/**
 * Created by xiongxl on 2022/3/16
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class NoopInterceptor1<K, V, A> implements Interceptor1<K, V, A> {
    private final boolean expectedResult;
    private static final NoopInterceptor1 NOOP = new NoopInterceptor1();

    public NoopInterceptor1() {
        expectedResult = true;
    }

    public NoopInterceptor1(boolean expectedResult) {
        this.expectedResult = expectedResult;
    }

    public static <K, V, A> Interceptor1<K, V, A> get() {
        return NOOP;
    }

    public static <K, V, A> Interceptor1<K, V, A> make(boolean expectedResult) {
        return new NoopInterceptor1(expectedResult);
    }

    @Override
    public Boolean intercept(K key, V value, A arg) {
        return expectedResult;
    }
}
