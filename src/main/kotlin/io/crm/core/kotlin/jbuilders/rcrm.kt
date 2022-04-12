package io.crm.core.kotlin.jbuilders

import io.crm.core.ReadCrm
import io.crm.core.builders.Builder

/**
 * Created by xiongxl in 2022/4/12
 */
class RCrm<K, V> : Builder<ReadCrm<K, V>> {
    private val tiers = arrayListOf<RTier<K, V>>()

    fun tier(init: RTier<K, V>.() -> Unit): RTier<K, V> {
        val tier = RTier<K, V>()
        tier.init()
        tiers.add(tier)
        return tier
    }

    override fun build(): ReadCrm<K, V> = ReadCrm(tiers.map { it.build() })
}

fun <K, V> rcrm(init: RCrm<K, V>.() -> Unit): ReadCrm<K, V> {
    val rc = RCrm<K, V>()
    rc.init()
    return rc.build()
}