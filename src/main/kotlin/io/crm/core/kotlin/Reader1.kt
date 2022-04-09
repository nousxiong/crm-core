package io.crm.core.kotlin

/**
 * Created by xiongxl in 2022/4/9 可挂起版[io.crm.core.Reader1]
 */
fun interface Reader1<K, V, A> {
    suspend fun read(key: K, arg: A): V?
}