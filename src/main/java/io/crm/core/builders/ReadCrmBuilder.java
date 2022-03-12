package io.crm.core.builders;

import io.crm.core.ReadCrm;
import io.crm.core.ReadTier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/12
 */
public class ReadCrmBuilder<K, V> implements Builder<ReadCrm<K, V>> {
    private final List<ReadTier<K, V>> readTiers = new ArrayList<>();

    private ReadCrmBuilder() {
    }

    public static <K, V> ReadCrmBuilder<K, V> newBuilder() {
        return new ReadCrmBuilder<>();
    }

    public ReadCrmBuilder<K, V> withLoadTier(ReadTier<K, V> readTier) {
        Objects.requireNonNull(readTier);
        readTiers.add(readTier);
        return this;
    }

    @Override
    public ReadCrm<K, V> build() {
        return new ReadCrm<>(readTiers);
    }
}
