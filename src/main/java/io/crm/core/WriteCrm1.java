package io.crm.core;

import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/16
 * 缓存关系写入类
 * @param <K>
 * @param <V>
 */
public class WriteCrm1<K, V, A> {
    private final List<WriteTier1<K, V, A>> writeTiers = new ArrayList<>();

    public WriteCrm1() {
    }

    public WriteCrm1(List<WriteTier1<K, V, A>> writeTiers) {
        if (writeTiers == null) {
            return;
        }
        for (WriteTier1<K, V, A> writeTier : writeTiers) {
            Objects.requireNonNull(writeTier);
        }
        this.writeTiers.addAll(writeTiers);
    }

    /**
     * 写入操作
     * @param key 键
     * @param value 要写入的值
     * @param arg 参数
     * @return 返回带有最新值的Future，这个最新值可能是其它并发写入操作的结果
     */
    public Future<V> write(K key, V value, A arg) {
        if (writeTiers.isEmpty()) {
            return Future.succeededFuture(value);
        }
        return write(0, key, value, arg);
    }

    private Future<V> write(int i, K key, V value, A arg) {
        if (i >= writeTiers.size()) {
            return Future.succeededFuture(value);
        }
        WriteTier1<K, V, A> tier = writeTiers.get(i);
        Interceptor1<K, V, A> interceptor = tier.getInterceptor();
        if (interceptor != null && !interceptor.intercept(key, value, arg)) {
            return write(i + 1, key, value, arg);
        }
        Writer1<K, V, A> writer = tier.getWriter();
        if (writer == null) {
            return write(i + 1, key, value, arg);
        }
        return writer.write(key, value, arg).compose(v -> write(i + 1, key, v, arg));
    }
}
