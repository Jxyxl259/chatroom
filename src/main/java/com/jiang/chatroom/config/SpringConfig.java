package com.jiang.chatroom.config;

import com.jiang.chatroom.config.ehcache.EhcacheConfig;
import net.sf.ehcache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;


public class SpringConfig {

    public EhCacheCacheManager springCacheCacheManager(){

        EhcacheConfig ehcacheConfig = new EhcacheConfig();

        CacheManager cacheManager = ehcacheConfig.cacheManager();

        EhCacheCacheManager SpringCacheManager = new EhCacheCacheManager(cacheManager);

        return SpringCacheManager;
    }

}