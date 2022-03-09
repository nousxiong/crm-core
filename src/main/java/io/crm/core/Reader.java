package io.crm.core;

import io.vertx.core.Future;

import java.util.function.Function;

/**
 * Created by xiongxl in 2022/3/9
 * 值读取器，返回带有值的Future，如果带的值null表示指定的键对应的值不存在
 * @param <K> 键类型
 * @param <V> 值类型
 */
public interface Reader<K, V> extends Function<K, Future<V>> {
}
