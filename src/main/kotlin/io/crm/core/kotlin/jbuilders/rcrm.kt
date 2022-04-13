package io.crm.core.kotlin.jbuilders

import io.crm.core.ReadCrm
import io.crm.core.ReadCrm1
import io.crm.core.builders.Builder

/**
 * Created by xiongxl in 2022/4/12
 */
@DslMarker
annotation class RCrmTagMarker

@RCrmTagMarker
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

@RCrmTagMarker
class RCrm1<K, V, A> : Builder<ReadCrm1<K, V, A>> {
    private val tiers = arrayListOf<RTier1<K, V, A>>()

    fun tier(init: RTier1<K, V, A>.() -> Unit): RTier1<K, V, A> {
        val tier = RTier1<K, V, A>()
        tier.init()
        tiers.add(tier)
        return tier
    }

    override fun build(): ReadCrm1<K, V, A> = ReadCrm1(tiers.map { it.build() })
}

fun <K, V> rcrm(init: RCrm<K, V>.() -> Unit): ReadCrm<K, V> {
    val rc = RCrm<K, V>()
    rc.init()
    return rc.build()
}

fun <K, V, A> rcrm1(init: RCrm1<K, V, A>.() -> Unit): ReadCrm1<K, V, A> {
    val rc = RCrm1<K, V, A>()
    rc.init()
    return rc.build()
}