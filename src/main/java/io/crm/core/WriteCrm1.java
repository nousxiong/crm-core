package io.crm.core;

import io.smallrye.mutiny.Uni;

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
    public Uni<V> write(K key, V value, A arg) {
        if (writeTiers.isEmpty()) {
            return Uni.createFrom().nullItem();
        }
        return write(0, key, value, arg);
    }

    private Uni<V> write(int i, K key, V value, A arg) {
        if (i >= writeTiers.size()) {
            return Uni.createFrom().nullItem();
        }

        WriteTier1<K, V, A> tier = writeTiers.get(i);
        Interceptor1<K, V, A> interceptor = tier.getInterceptor();
        Synchronizer1<K, A> synchronizer = tier.getSynchronizer1();

        if (synchronizer == null) {
            if (interceptor != null && !interceptor.intercept(key, value, arg)) {
                return write(i + 1, key, value, arg);
            }
            Writer1<K, V, A> writer = tier.getWriter();
            if (writer == null) {
                return write(i + 1, key, value, arg);
            }
            Uni<V> writeUni = writer.write(key, value, arg);
            if (i + 1 >= writeTiers.size()) {
                return writeUni;
            }
            return writeUni.onItem().transformToUni(v -> write(i + 1, key, value, arg));
        } else {
            return synchronizer.acquire(key, arg)
                    .onItem().transformToUni(lk -> {
                        if (interceptor != null && !interceptor.intercept(key, value, arg)) {
                            return write(i + 1, key, value, arg)
                                    .onItem().call(lk::release);
                        }
                        Writer1<K, V, A> writer = tier.getWriter();
                        if (writer == null) {
                            return write(i + 1, key, value, arg)
                                    .onItem().call(lk::release);
                        }
                        Uni<V> writeUni = writer.write(key, value, arg);
                        if (i + 1 >= writeTiers.size()) {
                            return writeUni.onItem().call(lk::release);
                        }
                        return writeUni.onItem().transformToUni(v -> write(i + 1, key, value, arg))
                                .onItem().call(lk::release);
                    });
        }
    }
}
