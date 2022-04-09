package io.crm.core;

import io.smallrye.mutiny.Uni;

/**
 * Created by xiongxl in 2022/3/25
 * 由同步器返回的锁
 */
@FunctionalInterface
public interface Locker {
    /**
     * 释放锁
     */
    Uni<Void> release();
}
