package io.crm.core;

import io.crm.core.noop.NoopArg;
import io.smallrye.mutiny.Uni;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongxl in 2022/3/16
 * 缓存关系写入类
 * @param <K>
 * @param <V>
 */
public class WriteCrm<K, V> extends WriteCrm1<K, V, NoopArg> {

    public WriteCrm() {
        super();
    }

    public WriteCrm(List<WriteTier<K, V>> writeTiers) {
        super(new ArrayList<>(writeTiers));
    }

    /**
     * 写入操作
     * @param key 键
     * @param value 要写入的值
     * @return 返回带有最新值的Future，这个最新值可能是其它并发写入操作的结果
     */
    public Uni<V> write(K key, V value) {
        return super.write(key, value, NoopArg.get());
    }
}
