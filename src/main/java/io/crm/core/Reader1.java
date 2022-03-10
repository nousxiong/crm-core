package io.crm.core;

import io.vertx.core.Future;

/**
 * Created by xiongxl in 2022/3/9
 * 带参数的值读取器
 * @param <K> 键类型
 * @param <V> 值类型
 * @param <A> 参数类型
 * @see Reader
 */
@FunctionalInterface
public interface Reader1<K, V, A> {
    Future<V> read(K key, A arg);
}
