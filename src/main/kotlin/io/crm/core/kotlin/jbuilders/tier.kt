package io.crm.core.kotlin.jbuilders

import io.crm.core.Cacher
import io.crm.core.Interceptor
import io.crm.core.ReadTier
import io.crm.core.Reader
import io.crm.core.Synchronizer
import io.crm.core.builders.Builder
import io.crm.core.noop.NoopCacher
import io.crm.core.noop.NoopInterceptor
import io.crm.core.noop.NoopReader

/**
 * Created by xiongxl in 2022/4/12
 */
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