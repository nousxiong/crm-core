package io.crm.core.builders;

import io.crm.core.WriteCrm1;
import io.crm.core.WriteTier1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by xiongxl in 2022/4/9
 */
public class WriteCrm1Builder<K, V, A> implements Builder<WriteCrm1<K, V, A>> {
    private final List<WriteTier1<K, V, A>> writeTiers = new ArrayList<>();

    private WriteCrm1Builder() {
    }

    public static <K, V, A> WriteCrm1Builder<K, V, A> newBuilder(Class<K> keyType, Class<V> valueType, Class<A> argType) {
        return new WriteCrm1Builder<>();
    }

    public WriteCrm1Builder<K, V, A> withWriteTier(WriteTier1<K, V, A> writeTier) {
        Objects.requireNonNull(writeTier);
        writeTiers.add(writeTier);
        return this;
    }

    @Override
    public WriteCrm1<K, V, A> build() {
        return new WriteCrm1<>(writeTiers);
    }
}
