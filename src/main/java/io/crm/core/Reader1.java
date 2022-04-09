package io.crm.core;

import io.smallrye.mutiny.Uni;

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
    /**
     * 读取操作
     * @param key 键
     * @param arg 参数
     * @return 返回带有值的Future，如果带的值null表示指定的键对应的值不存在
     */
    Uni<V> read(K key, A arg);
}
