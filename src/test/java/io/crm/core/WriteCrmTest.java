package io.crm.core;

import io.crm.core.builders.WriteCrmBuilder;
import io.crm.core.builders.WriteTierBuilder;
import io.vertx.core.Future;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WriteCrmTest {

    private static class MyValue {
        int id;
        String name;

        public MyValue(int id, String name) {
            Objects.requireNonNull(name);
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyValue myValue = (MyValue) o;
            return id == myValue.id && name.equals(myValue.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }

    private WriteCrm<String, MyValue> wcrmOfWriteThrough(
            Map<String, MyValue> cacheSource,
            Map<String, MyValue> sysOfRecSource
    ) {
        return WriteCrmBuilder.newBuilder(String.class, MyValue.class)
                .withWriteTier(
                        WriteTierBuilder.newBuilder(String.class, MyValue.class)
                                .withWriter((key, value) -> Future.succeededFuture(sysOfRecSource.compute(key, (ck, cv) -> value)))
                                .build()
                )
                .withWriteTier(
                        WriteTierBuilder.newBuilder(String.class, MyValue.class)
                                .withWriter((key, value) -> Future.succeededFuture(cacheSource.compute(key, (ck, cv) -> value)))
                                .build()
                )
                .build();
    }

    @Test
    void testWriteCrmOfWriteThrough() {
        // 模拟Cache源
        HashMap<String, MyValue> cacheSource = new HashMap<>();
        // 模拟SoR源
        HashMap<String, MyValue> sysOfRecSource = new HashMap<>();

        // Write Through
        WriteCrm<String, MyValue> wcrm = wcrmOfWriteThrough(cacheSource, sysOfRecSource);

        // 准备测试数据
        final int tcnt = 10;
        List<String> keys = new ArrayList<>();
        List<MyValue> values = new ArrayList<>();
        for (int i = 0; i < tcnt; i++) {
            String key = "key-" + i;
            keys.add(key);
            values.add(new MyValue(key.hashCode(), key));
        }

        // 写入指定key数据
        List<Integer> indexs = new ArrayList<>(tcnt);
        for (int i = 0; i < tcnt; i++) {
            indexs.add(i);
        }
        Collections.shuffle(indexs);

        for (int index : indexs) {
            String key = keys.get(index);
            MyValue value = values.get(index);
            assertEquals(value, wcrm.write(key, value).result());
            assertEquals(value, cacheSource.get(key));
            assertEquals(value, sysOfRecSource.get(key));
        }
    }
}