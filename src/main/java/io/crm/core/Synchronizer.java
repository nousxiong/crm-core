package io.crm.core;

import io.vertx.core.Future;

/**
 * Created by xiongxl in 2022/3/25
 */
@FunctionalInterface
public interface Synchronizer<K> {
    Future<Locker> acquire(K key);
}
