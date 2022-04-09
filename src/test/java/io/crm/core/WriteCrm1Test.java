package io.crm.core;

import io.crm.core.builders.WriteCrm1Builder;
import io.crm.core.builders.WriteTier1Builder;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class WriteCrm1Test {

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

    private static class MyArg {
        int len;
        String options;

        public MyArg(int len, String options) {
            this.len = len;
            this.options = options;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyArg myArg = (MyArg) o;
            return len == myArg.len && options.equals(myArg.options);
        }

        @Override
        public int hashCode() {
            return Objects.hash(len, options);
        }
    }

    private WriteCrm1<String, MyValue, MyArg> wcrmOfWriteThrough(
            Map<String, MyValue> cacheSource,
            Map<String, MyValue> sysOfRecSource,
            Interceptor1<String, MyValue, MyArg> picker
    ) {
        return WriteCrm1Builder.newBuilder(String.class, MyValue.class, MyArg.class)
                .withWriteTier(
                        WriteTier1Builder.newBuilder(String.class, MyValue.class, MyArg.class)
                                .withWriter((key, value, arg) -> Uni.createFrom().item(
                                        sysOfRecSource.compute(key, (ck, cv) -> value)
                                ))
                                .withInterceptor(picker)
                                .build()
                )
                .withWriteTier(
                        WriteTier1Builder.newBuilder(String.class, MyValue.class, MyArg.class)
                                .withWriter((key, value, arg) -> {
                                    cacheSource.remove(key);
                                    return Uni.createFrom().item(value);
                                })
                                .withInterceptor(picker)
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
        WriteCrm1<String, MyValue, MyArg> wcrm = wcrmOfWriteThrough(
                cacheSource,
                sysOfRecSource,
                (k, v, a) -> Math.abs(k.hashCode()) % 2 == 0
        );

        // 准备测试数据
        final int tcnt = 10;
        List<String> keys = new ArrayList<>();
        List<MyValue> values = new ArrayList<>();
        List<MyArg> args = new ArrayList<>();
        for (int i = 0; i < tcnt; i++) {
            String key = "key-" + i;
            keys.add(key);
            values.add(new MyValue(key.hashCode(), key));
            args.add(new MyArg(key.length(), "opts-" + key));
        }

        // 准备Cache数据
        for (int i = 0; i < tcnt; i++) {
            cacheSource.put(keys.get(i), values.get(i));
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
            MyArg arg = args.get(index);
            if (Math.abs(key.hashCode()) % 2 == 0) {
                assertEquals(value, wcrm.write(key, value, arg).await().indefinitely());
                assertNull(cacheSource.get(key));
                assertEquals(value, sysOfRecSource.get(key));
            } else {
                assertNull(wcrm.write(key, value, arg).await().indefinitely());
                assertEquals(value, cacheSource.get(key));
                assertNull(sysOfRecSource.get(key));
            }
        }
    }
}