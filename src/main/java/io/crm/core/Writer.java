package io.crm.core;

import io.vertx.core.Future;

/**
 * Created by xiongxl in 2022/3/15
 * 值写入器
 *  @param <K> 键类型
 *  @param <V> 值类型
 */
@FunctionalInterface
public interface Writer<K, V> {
    /**
     * 写入操作
     * @param key 指定的键
     * @param value 要保存的值
     * @return 返回带有最新值的Future，这个最新值可能是其它并发write/cache操作进行的结果（比如比当前write时指定的值要新）
     */
    Future<V> write(K key, V value);
}
