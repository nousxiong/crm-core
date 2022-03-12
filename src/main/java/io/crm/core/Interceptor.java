package io.crm.core;

/**
 * Created by xiongxl in 2022/3/12
 * 值拦截器，用于对值更新（缓存/存储）之前进行拦截
 * @param <K> 键类型
 * @param <V> 值类型
 * @see Cacher
 */
@FunctionalInterface
public interface Interceptor<K, V> {
    /**
     * 拦截操作
     * @param key 指定键
     * @param value 指定值
     * @return 返回true表示可以更新，false表示不可更新
     */
    Boolean intercept(K key, V value);
}
