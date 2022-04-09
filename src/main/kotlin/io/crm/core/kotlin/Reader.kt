package io.crm.core.kotlin

import io.crm.core.Reader
import io.crm.core.Reader1
import io.crm.core.noop.NoopArg

/**
 * Created by xiongxl in 2022/4/9 可挂起版[io.crm.core.Reader]
 */
fun interface Reader<K, V> {
    suspend fun read(key: K): V?

    companion object {
        fun <K, V> toReader1(reader: Reader<K, V>?): Reader1<K, V, NoopArg>? {
            return if (reader == null) {
                null
            } else Reader1 { key: K, _: NoopArg -> reader.read(key) }
        }
    }
}