package io.crm.core;

import io.smallrye.mutiny.Uni;

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
    /**
     * 缓存指定的值
     * @param key 键
     * @param value 值
     * @param arg 参数
     * @return 返回带有最新值的Future，这个最新值可能是其它并发write/cache操作进行的结果（比如比当前write时指定的值要新）
     */
    Uni<V> cache(K key, V value, A arg);
}
