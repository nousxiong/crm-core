package io.crm.core;

import io.crm.core.noop.NoopArg;
import io.smallrye.mutiny.Uni;

/**
 * Created by xiongxl in 2022/3/25
 */
@FunctionalInterface
public interface Synchronizer<K> {
    /**
     * 获取锁
     * @param key 键
     * @return 返回锁
     */
    Uni<Locker> acquire(K key);

    static <K> Synchronizer1<K, NoopArg> toSynchronizer1(Synchronizer<K> synchronizer) {
        if (synchronizer == null) {
            return null;
        }
        return (key, arg) -> synchronizer.acquire(key);
    }
}
