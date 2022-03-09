package io.crm.core;

import io.vertx.core.Future;

/**
 * Created by xiongxl in 2022/3/9
 * @param <K>
 * @param <V>
 */
public class ReadCrm<K, V> {
    public Future<V> read(K key) {
        return Future.succeededFuture();
    }
}
