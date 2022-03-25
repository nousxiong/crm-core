package io.crm.core;

import io.vertx.core.Future;

/**
 * Created by xiongxl in 2022/3/25
 */
@FunctionalInterface
public interface Synchronizer<K> {
    /**
     * 获取锁
     * @param key 键
     * @return 返回带有锁
     */
    Future<Locker> acquire(K key);
}
