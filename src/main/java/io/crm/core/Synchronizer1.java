package io.crm.core;

import io.vertx.core.Future;

/**
 * Created by xiongxl on 2022/4/2
 */
@FunctionalInterface
public interface Synchronizer1<K, A> {
    /**
     * 获取锁
     * @param key 键
     * @param arg 参数
     * @return 返回锁
     */
    Future<Locker> acquire(K key, A arg);
}
