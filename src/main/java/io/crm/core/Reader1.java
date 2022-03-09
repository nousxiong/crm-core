package io.crm.core;

import io.vertx.core.Future;

import java.util.function.BiFunction;

/**
 * Created by xiongxl in 2022/3/9
 * 带参数的值读取器
 * @param <K> 键类型
 * @param <V> 值类型
 * @param <A> 参数类型
 * @see Reader
 */
public interface Reader1<K, V, A> extends BiFunction<K, A, Future<V>> {
}
