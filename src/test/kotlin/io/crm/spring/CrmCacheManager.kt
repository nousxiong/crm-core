package io.crm.spring

import org.springframework.cache.Cache
import org.springframework.cache.CacheManager

/**
 * Created by xiongxl in 2022/5/22
 */
class CrmCacheManager : CacheManager {
    override fun getCache(name: String): Cache? {
        TODO("Not yet implemented")
    }

    override fun getCacheNames(): MutableCollection<String> {
        TODO("Not yet implemented")
    }
}