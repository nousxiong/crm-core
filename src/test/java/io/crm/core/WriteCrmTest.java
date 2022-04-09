package io.crm.core;

import io.crm.core.builders.WriteCrmBuilder;
import io.crm.core.builders.WriteTierBuilder;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class WriteCrmTest {

    private static class WcrmLocker implements Locker {
        public int getLock() {
            return lock;
        }

        private int lock = 0;

        public Locker lock() {
            lock++;
            return this;
        }

        @Override
        public Uni<Void> release() {
            lock++;
            return Uni.createFrom().nullItem();
        }
    }

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
            Map<String, MyValue> sysOfRecSource,
            Interceptor<String, MyValue> picker,
            Synchronizer<String> cacheSyncr,
            Synchronizer<String> sorSyncr
    ) {
        return WriteCrmBuilder.newBuilder(String.class, MyValue.class)
                .withWriteTier(
                        WriteTierBuilder.newBuilder(String.class, MyValue.class)
                                .withWriter((key, value) -> Uni.createFrom().item(
                                        sysOfRecSource.compute(key, (ck, cv) -> value)
                                ))
                                .withInterceptor(picker)
                                .withSynchronizer(sorSyncr)
                                .build()
                )
                .withWriteTier(
                        WriteTierBuilder.newBuilder(String.class, MyValue.class)
                                .withWriter((key, value) -> {
                                    cacheSource.remove(key);
                                    return Uni.createFrom().item(value);
                                })
                                .withInterceptor(picker)
                                .withSynchronizer(cacheSyncr)
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
        WcrmLocker cacheLocker = new WcrmLocker();
        WcrmLocker sorLocker = new WcrmLocker();

        // Write Through
        WriteCrm<String, MyValue> wcrm = wcrmOfWriteThrough(
                cacheSource,
                sysOfRecSource,
                (k, v) -> Math.abs(k.hashCode()) % 2 == 0,
                (k) -> Uni.createFrom().item(cacheLocker.lock()),
                (k) -> Uni.createFrom().item(sorLocker.lock())
        );

        // 准备测试数据
        final int tcnt = 10;
        List<String> keys = new ArrayList<>();
        List<MyValue> values = new ArrayList<>();
        for (int i = 0; i < tcnt; i++) {
            String key = "key-" + i;
            keys.add(key);
            values.add(new MyValue(key.hashCode(), key));
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

        int locker = 0;
        for (int index : indexs) {
            String key = keys.get(index);
            MyValue value = values.get(index);
            locker += 2;
            if (Math.abs(key.hashCode()) % 2 == 0) {
                assertEquals(value, wcrm.write(key, value).await().indefinitely());
                assertNull(cacheSource.get(key));
                assertEquals(value, sysOfRecSource.get(key));
            } else {
                assertNull(wcrm.write(key, value).await().indefinitely());
                assertEquals(value, cacheSource.get(key));
                assertNull(sysOfRecSource.get(key));
            }
            assertEquals(locker, cacheLocker.getLock());
            assertEquals(locker, sorLocker.getLock());
        }
    }
}