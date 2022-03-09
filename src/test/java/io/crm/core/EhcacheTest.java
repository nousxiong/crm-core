package io.crm.core;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EhcacheTest {
    @Test
    public void testEhcache() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().withCache("preConfigured",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                        Long.class, String.class, ResourcePoolsBuilder.heap(100)
                ).build()
        ).build(true);

        Cache<Long, String> preConfigured = cacheManager.getCache("preConfigured", Long.class, String.class);
        assertNull(preConfigured.get(1L));
    }
}
