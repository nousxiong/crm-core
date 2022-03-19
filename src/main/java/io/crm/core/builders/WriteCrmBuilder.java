package io.crm.core.builders;

import io.crm.core.WriteCrm;
import io.crm.core.WriteTier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/19
 */
public class WriteCrmBuilder<K, V> implements Builder<WriteCrm<K, V>> {
    private final List<WriteTier<K, V>> writeTiers = new ArrayList<>();

    private WriteCrmBuilder() {
    }

    public static <K, V> WriteCrmBuilder<K, V> newBuilder(Class<K> keyType, Class<V> valueType) {
        return new WriteCrmBuilder<>();
    }

    public WriteCrmBuilder<K, V> withWriteTier(WriteTier<K, V> writeTier) {
        Objects.requireNonNull(writeTier);
        writeTiers.add(writeTier);
        return this;
    }

    @Override
    public WriteCrm<K, V> build() {
        return new WriteCrm<>(writeTiers);
    }
}
