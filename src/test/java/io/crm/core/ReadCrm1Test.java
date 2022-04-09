package io.crm.core;

import io.crm.core.builders.ReadCrm1Builder;
import io.crm.core.builders.ReadTier1Builder;
import io.crm.core.noop.NoopInterceptor1;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReadCrm1Test {
    private enum FromType {
        NONE,
        CACHE,
        SOR
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

    /**
     * 构建Read Through读映射器
     *
     * @param cacheSource    Cache源
     * @param sysOfRecSource SoR源
     * @return 构建好的Read Through读映射器
     */
    private ReadCrm1<String, MyValue, MyArg> rcrmOfReadThrough(
            Map<String, MyValue> cacheSource,
            Map<String, MyValue> sysOfRecSource
    ) {
        return rcrmOfCachePick(cacheSource, sysOfRecSource, NoopInterceptor1.get());
    }

    /**
     * 构建带有cache选择器的Read Through读映射器
     *
     * @param cacheSource    Cache源
     * @param sysOfRecSource SoR源
     * @param picker         缓存选择器
     * @return 构建好的Read Through读映射器
     */
    private ReadCrm1<String, MyValue, MyArg> rcrmOfCachePick(
            Map<String, MyValue> cacheSource,
            Map<String, MyValue> sysOfRecSource,
            Interceptor1<String, MyValue, MyArg> picker
    ) {
        return ReadCrm1Builder.newBuilder(String.class, MyValue.class, MyArg.class)
                .withReadTier(
                        ReadTier1Builder.newBuilder(String.class, MyValue.class, MyArg.class)
                                .withReader((key, arg) -> {
                                    MyValue cvalue = cacheSource.get(key);
                                    if (cvalue != null) {
                                        cvalue.updateFrom(FromType.CACHE);
                                    }
                                    return Uni.createFrom().item(cvalue);
                                })
                                .withCacher((key, value, arg) -> Uni.createFrom().item(cacheSource.compute(key, (ck, cv) -> value)))
                                .withInterceptor(picker)
                                .build()
                )
                .withReadTier(
                        ReadTier1Builder.newBuilder(String.class, MyValue.class, MyArg.class)
                                .withReader((key, arg) -> {
                                    MyValue sorValue = sysOfRecSource.get(key);
                                    if (sorValue != null) {
                                        sorValue.updateFrom(FromType.SOR);
                                    }
                                    return Uni.createFrom().item(sorValue);
                                })
                                .build()
                )
                .build();
    }

    @Test
    void testReadCrmOfReadThrough() {
        // 模拟Cache源
        HashMap<String, MyValue> cacheSource = new HashMap<>();
        // 模拟SoR源
        HashMap<String, MyValue> sysOfRecSource = new HashMap<>();

        // ReadThrough
        ReadCrm1<String, MyValue, MyArg> rcrm = rcrmOfReadThrough(cacheSource, sysOfRecSource);

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
            MyArg arg = args.get(index);
            assertEquals(new MyValue(key.hashCode(), key, FromType.SOR), rcrm.read(key, arg).await().indefinitely());
            assertEquals(new MyValue(key.hashCode(), key, FromType.CACHE), rcrm.read(key, arg).await().indefinitely());
        }
    }

    @Test
    public void testReadCrmOfCachePick() {
        // 模拟Cache源
        HashMap<String, MyValue> cacheSource = new HashMap<>();
        // 模拟SoR源
        HashMap<String, MyValue> sysOfRecSource = new HashMap<>();

        // ReadThrough + 只选择key的hashCode能整除2的value进行缓存
        ReadCrm1<String, MyValue, MyArg> rcrm = rcrmOfCachePick(
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
            MyArg arg = args.get(index);
            assertEquals(new MyValue(key.hashCode(), key, FromType.SOR), rcrm.read(key, arg).await().indefinitely());
            FromType fromType = FromType.SOR;
            if (Math.abs(key.hashCode()) % 2 == 0) {
                fromType = FromType.CACHE;
            }
            assertEquals(new MyValue(key.hashCode(), key, fromType), rcrm.read(key, arg).await().indefinitely());
        }
    }
}