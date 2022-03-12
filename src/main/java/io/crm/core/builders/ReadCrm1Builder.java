package io.crm.core.builders;

import io.crm.core.ReadCrm1;
import io.crm.core.ReadTier1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by xiongxl in 2022/3/12
 */
public class ReadCrm1Builder<K, V, A> implements Builder<ReadCrm1<K, V, A>> {
    private final List<ReadTier1<K, V, A>> readTiers = new ArrayList<>();

    private ReadCrm1Builder() {
    }

    public static <K, V, A> ReadCrm1Builder<K, V, A> newBuilder() {
        return new ReadCrm1Builder<>();
    }

    public ReadCrm1Builder<K, V, A> withLoadTier(ReadTier1<K, V, A> readTier) {
        Objects.requireNonNull(readTier);
        readTiers.add(readTier);
        return this;
    }

    @Override
    public ReadCrm1<K, V, A> build() {
        return new ReadCrm1<>(readTiers);
    }
}
