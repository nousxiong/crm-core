package io.crm.core;

import io.smallrye.mutiny.Uni;

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
    Uni<Locker> acquire(K key, A arg);
}
