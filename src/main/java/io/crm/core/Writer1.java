package io.crm.core;

import io.vertx.core.Future;

/**
 * Created by xiongxl on 2022/3/16
 */
@FunctionalInterface
public interface Writer1<K, V, A> {
    /**
     * 写入操作
     * @param key 指定的键
     * @param value 要保存的值
     * @param arg 参数
     * @return 返回带有最新值的Future，这个最新值可能是其它并发write/cache操作进行的结果（比如比当前write时指定的值要新）
     */
    Future<V> write(K key, V value, A arg);
}
