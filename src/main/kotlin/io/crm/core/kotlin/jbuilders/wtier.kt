package io.crm.core.kotlin.jbuilders

import io.crm.core.Interceptor
import io.crm.core.Synchronizer
import io.crm.core.WriteTier
import io.crm.core.Writer
import io.crm.core.builders.Builder
import io.crm.core.noop.NoopInterceptor
import io.crm.core.noop.NoopWriter

/**
 * Created by xiongxl in 2022/4/27
 */
class WTier<K, V> : Builder<WriteTier<K, V>> {
    private var writer = NoopWriter.get<K, V>()
    private var interceptor = NoopInterceptor.get<K, V>()
    private var synchronizer: Synchronizer<K>? = null

    fun writer(writer: Writer<K, V>) {
        this.writer = writer
    }

    fun interceptor(interceptor: Interceptor<K, V>) {
        this.interceptor = interceptor
    }

    fun synchronizer(synchronizer: Synchronizer<K>) {
        this.synchronizer = synchronizer
    }

    override fun build(): WriteTier<K, V> = WriteTier(writer, interceptor, synchronizer)

}