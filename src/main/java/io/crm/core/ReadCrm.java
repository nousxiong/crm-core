package io.crm.core;

import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongxl in 2022/3/9
 * 缓存关系读取类
 * @param <K>
 * @param <V>
 */
public class ReadCrm<K, V> extends ReadCrm1<K, V, Void> {

    public ReadCrm() {
    }

    public ReadCrm(List<ReadTier<K, V>> readTiers) {
        super(new ArrayList<>(readTiers));
    }

    /**
     * 读取操作
     * @param key 键
     * @return Future值
     */
    public Future<V> read(K key) {
        return super.read(key, null);
    }
}
