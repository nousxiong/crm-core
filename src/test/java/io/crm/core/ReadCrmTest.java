package io.crm.core;

import io.crm.core.builders.ReadCrmBuilder;
import io.crm.core.builders.ReadTierBuilder;
import io.crm.core.noop.NoopInterceptor;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadCrmTest {
    private enum FromType {
        NONE,
        CACHE,
        SOR
    }

    private static class RcrmLocker implements Locker {
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
        FromType from = FromType.NONE;

        public MyValue(int id, String name) {
            Objects.requireNonNull(name);
            this.id = id;
            this.name = name;
        }

        public MyValue(int id, String name, FromType from) {
            this(id, name);
            Objects.requireNonNull(from);
            this.from = from;
        }

        public void updateFrom(FromType from) {
            Objects.requireNonNull(from);
            this.from = from;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MyValue myValue = (MyValue) o;
            return id == myValue.id && name.equals(myValue.name) && from == myValue.from;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, from);
        }
    }

    /**
     * 构建Read Through读映射器
     *
     * @param cacheSource    Cache源
     * @param sysOfRecSource SoR源
     * @return 构建好的Read Through读映射器
     */
    private ReadCrm<String, MyValue> rcrmOfReadThrough(
            Map<String, MyValue> cacheSource,
            Map<String, MyValue> sysOfRecSource
    ) {
        return rcrmOfCachePick(
                cacheSource, sysOfRecSource, NoopInterceptor.get(), null, null
        );
    }

    /**
     * 构建带有cache选择器的Read Through读映射器
     *
     * @param cacheSource    Cache源
     * @param sysOfRecSource SoR源
     * @param picker         缓存选择器
     * @return 构建好的Read Through读映射器
     */
    private ReadCrm<String, MyValue> rcrmOfCachePick(
            Map<String, MyValue> cacheSource,
            Map<String, MyValue> sysOfRecSource,
            Interceptor<String, MyValue> picker,
            Synchronizer<String> cacheSyncr,
            Synchronizer<String> sorSyncr
    ) {
        return ReadCrmBuilder.newBuilder(String.class, MyValue.class)
                .withReadTier(
                        ReadTierBuilder.newBuilder(String.class, MyValue.class)
                                .withReader((key) -> {
                                    MyValue cvalue = cacheSource.get(key);
                                    if (cvalue != null) {
                                        cvalue.updateFrom(FromType.CACHE);
                                    }
                                    return Uni.createFrom().item(cvalue);
                                })
                                .withCacher((key, value) -> Uni.createFrom().item(cacheSource.compute(key, (ck, cv) -> value)))
                                .withInterceptor(picker)
                                .withSynchronizer(cacheSyncr)
                                .build()
                )
                .withReadTier(
                        ReadTierBuilder.newBuilder(String.class, MyValue.class)
                                .withReader((key) -> {
                                    MyValue sorValue = sysOfRecSource.get(key);
                                    if (sorValue != null) {
                                        sorValue.updateFrom(FromType.SOR);
                                    }
                                    return Uni.createFrom().item(sorValue);
                                })
                                .withSynchronizer(sorSyncr)
                                .build()
                )
                .build();
    }

    @Test
    public void testReadCrmOfReadThrough() {
        // 模拟Cache源
        HashMap<String, MyValue> cacheSource = new HashMap<>();
        // 模拟SoR源
        HashMap<String, MyValue> sysOfRecSource = new HashMap<>();

        // ReadThrough
        ReadCrm<String, MyValue> rcrm = rcrmOfReadThrough(cacheSource, sysOfRecSource);

        // 准备测试数据
        final int tcnt = 10;
        List<String> keys = new ArrayList<>();
        List<MyValue> values = new ArrayList<>();
        for (int i = 0; i < tcnt; i++) {
            String key = "key-" + i;
            keys.add(key);
            values.add(new MyValue(key.hashCode(), key));
        }

        // 准备SoR数据
        for (int i = 0; i < tcnt; i++) {
            sysOfRecSource.put(keys.get(i), values.get(i));
        }

        // 读取指定key数据
        List<Integer> indexs = new ArrayList<>(tcnt);
        for (int i = 0; i < tcnt; i++) {
            indexs.add(i);
        }
        Collections.shuffle(indexs);
        for (int index : indexs) {
            String key = keys.get(index);
            assertEquals(new MyValue(key.hashCode(), key, FromType.SOR), rcrm.read(key).await().indefinitely());
            assertEquals(new MyValue(key.hashCode(), key, FromType.CACHE), rcrm.read(key).await().indefinitely());
        }
    }

    @Test
    public void testReadCrmOfCachePick() {
        // 模拟Cache源
        HashMap<String, MyValue> cacheSource = new HashMap<>();
        // 模拟SoR源
        HashMap<String, MyValue> sysOfRecSource = new HashMap<>();
        RcrmLocker cacheLocker = new RcrmLocker();
        RcrmLocker sorLocker = new RcrmLocker();

        assertEquals(0, cacheLocker.getLock());
        assertEquals(0, sorLocker.getLock());

        // ReadThrough + 只选择key的hashCode能整除2的value进行缓存
        ReadCrm<String, MyValue> rcrm = rcrmOfCachePick(
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

        // 准备SoR数据
        for (int i = 0; i < tcnt; i++) {
            sysOfRecSource.put(keys.get(i), values.get(i));
        }

        // 读取指定key数据
        List<Integer> indexs = new ArrayList<>(tcnt);
        for (int i = 0; i < tcnt; i++) {
            indexs.add(i);
        }
        Collections.shuffle(indexs);
        int i = 0;
        int sorLock = 0;
        for (int index : indexs) {
            String key = keys.get(index);
            FromType fromType = FromType.SOR;
            if (Math.abs(key.hashCode()) % 2 == 0) {
                fromType = FromType.CACHE;
            }

            assertEquals(new MyValue(key.hashCode(), key, FromType.SOR), rcrm.read(key).await().indefinitely());
            sorLock += 2;
            assertEquals(i * 4 + 2, cacheLocker.getLock());
            assertEquals(sorLock, sorLocker.getLock());

            assertEquals(new MyValue(key.hashCode(), key, fromType), rcrm.read(key).await().indefinitely());
            if (fromType == FromType.SOR) {
                sorLock += 2;
            }
            assertEquals(i * 4 + 4, cacheLocker.getLock());
            assertEquals(sorLock, sorLocker.getLock());
            i++;
        }
    }
}