package io.crm.core;

/**
 * Created by xiongxl in 2022/3/12
 * 带参数的值拦截器
 * @see Interceptor
 */
@FunctionalInterface
public interface Interceptor1<K, V, A> {
    /**
     * 拦截操作
     * @param key 指定键
     * @param value 指定值
     * @param arg 指定参数
     * @return 返回true表示可以更新，false表示不可更新
     */
    Boolean intercept(K key, V value, A arg);
}
