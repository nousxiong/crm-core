package io.crm.core;

import io.crm.core.noop.NoopArg;
import io.smallrye.mutiny.Uni;

/**
 * Created by xiongxl in 2022/3/9
 * 值读取器，返回带有值的Future，如果带的值null表示指定的键对应的值不存在
 * @param <K> 键类型
 * @param <V> 值类型
 */
@FunctionalInterface
public interface Reader<K, V> {
    /**
     * 读取操作
     * @param key 指定键
     * @return 返回带有值的Future，如果带的值null表示指定的键对应的值不存在
     */
    Uni<V> read(K key);

    static <K, V> Reader1<K, V, NoopArg> toReader1(Reader<K, V> reader) {
        if (reader == null) {
            return null;
        }
        return (key, arg) -> reader.read(key);
    }
}
