package io.crm.core;

import io.vertx.core.Future;

/**
 * Created by xiongxl in 2022/3/9
 * 值缓存器，返回带有最新值的Future，这个最新值可能是其它write/cache操作进行的结果（比如比当前write时指定的值要新）
 * @param <K> 键类型
 * @param <V> 值类型
 */
@FunctionalInterface
public interface Cacher<K, V> {
    Future<V> cache(K key, V value);
}
