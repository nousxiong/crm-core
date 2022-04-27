package io.crm.core.kotlin.jbuilders

import io.crm.core.WriteCrm
import io.crm.core.builders.Builder

/**
 * Created by xiongxl in 2022/4/27
 */
@DslMarker
annotation class WCrmTagMarker

@WCrmTagMarker
class WCrm<K, V> : Builder<WriteCrm<K, V>> {
    private val tiers = arrayListOf<WTier<K, V>>()

    fun tier(init: WTier<K, V>.() -> Unit): WTier<K, V> {
        val tier = WTier<K, V>()
        tier.init()
        tiers.add(tier)
        return tier
    }

    override fun build(): WriteCrm<K, V> = WriteCrm(tiers.map { it.build() })
}

fun <K, V> wcrm(init: WCrm<K, V>.() -> Unit): WriteCrm<K, V> {
    val wc = WCrm<K, V>()
    wc.init()
    return wc.build()
}