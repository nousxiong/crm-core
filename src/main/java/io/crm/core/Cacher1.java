package io.crm.core;

import io.vertx.core.Future;

/**
 * Created by xiongxl in 2022/3/9
 * 带参数的值缓存器
 * @param <K> 键类型
 * @param <V> 值类型
 * @param <A> 参数类型
 * @see Cacher
 */
@FunctionalInterface
public interface Cacher1<K, V, A> {
    Future<V> cache(K key, V value, A arg);

    default Cacher<K, V> toCacher() {
        return (key, value) -> this.cache(key, value, null);
    }
}
