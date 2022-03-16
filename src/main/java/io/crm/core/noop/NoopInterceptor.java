package io.crm.core.noop;

import io.crm.core.Interceptor;

/**
 * Created by xiongxl on 2022/3/16
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class NoopInterceptor<K, V> implements Interceptor<K, V> {
    private final boolean expectedResult;
    private static final NoopInterceptor NOOP = new NoopInterceptor();

    public NoopInterceptor() {
        expectedResult = true;
    }

    public NoopInterceptor(boolean expectedResult) {
        this.expectedResult = expectedResult;
    }

    public static <K, V> Interceptor<K, V> get() {
        return NOOP;
    }

    public static <K, V> Interceptor<K, V> make(boolean expectedResult) {
        return new NoopInterceptor(expectedResult);
    }

    @Override
    public Boolean intercept(K key, V value) {
        return expectedResult;
    }
}
