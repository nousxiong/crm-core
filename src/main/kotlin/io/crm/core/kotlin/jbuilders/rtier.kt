package io.crm.core.kotlin.jbuilders

import io.crm.core.*
import io.crm.core.builders.Builder
import io.crm.core.noop.*

/**
 * Created by xiongxl in 2022/4/12
 */
@RCrmTagMarker
class RTier<K, V> : Builder<ReadTier<K, V>> {
    private var reader = NoopReader.get<K, V>()
    private var cacher = NoopCacher.get<K, V>()
    private var interceptor = NoopInterceptor.get<K, V>()
    private var synchronizer: Synchronizer<K>? = null

    fun reader(reader: Reader<K, V>) {
        this.reader = reader
    }

    fun cacher(cacher: Cacher<K, V>) {
        this.cacher = cacher
    }

    fun interceptor(interceptor: Interceptor<K, V>) {
        this.interceptor = interceptor
    }

    fun synchronizer(synchronizer: Synchronizer<K>) {
         this.synchronizer = synchronizer
    }

    override fun build(): ReadTier<K, V> = ReadTier(reader, cacher, interceptor, synchronizer)
}

@RCrmTagMarker
class RTier1<K, V, A> : Builder<ReadTier1<K, V, A>> {
    private var reader = NoopReader1.get<K, V, A>()
    private var cacher = NoopCacher1.get<K, V, A>()
    private var interceptor = NoopInterceptor1.get<K, V, A>()
    private var synchronizer: Synchronizer1<K, A>? = null

    fun reader(reader: Reader1<K, V, A>) {
        this.reader = reader
    }

    fun cacher(cacher: Cacher1<K, V, A>) {
        this.cacher = cacher
    }

    fun interceptor(interceptor: Interceptor1<K, V, A>) {
        this.interceptor = interceptor
    }

    fun synchronizer(synchronizer: Synchronizer1<K, A>) {
        this.synchronizer = synchronizer
    }

    override fun build(): ReadTier1<K, V, A> = ReadTier1(reader, cacher, interceptor, synchronizer)
}