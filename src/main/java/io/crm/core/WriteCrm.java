package io.crm.core;

import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongxl in 2022/3/16
 * 缓存关系写入类
 * @param <K>
 * @param <V>
 */
public class WriteCrm<K, V> {
    private final List<WriteTier<K, V>> writeTiers = new ArrayList<>();

    public WriteCrm() {
    }

    public WriteCrm(List<WriteTier<K, V>> writeTiers) {
        if (writeTiers == null) return;
        this.writeTiers.addAll(writeTiers);
    }

    /**
     * 写入操作
     * @param key 键
     * @param value 要写入的值
     * @return 返回带有最新值的Future，这个最新值可能是其它并发写入操作的结果
     */
    public Future<V> write(K key, V value) {
        if (writeTiers.isEmpty()) return Future.succeededFuture(value);
        return write(0, key, value);
    }

    private Future<V> write(int i, K key, V value) {
        if (i >= writeTiers.size()) return Future.succeededFuture(value);
        WriteTier<K, V> tier = writeTiers.get(i);
        if (!tier.getInterceptor().intercept(key, value)) return write(i + 1, key, value);
        return tier.getWriter().write(key, value).compose(v -> write(i + 1, key, v));
    }
}
