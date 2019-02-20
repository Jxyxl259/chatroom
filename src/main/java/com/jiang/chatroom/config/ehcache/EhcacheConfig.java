package com.jiang.chatroom.config.ehcache;

import net.sf.ehcache.CacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.core.io.ClassPathResource;

/**
 * @description: ehcache 缓存配置类
 * @author:
 * @create: 2018-07-08 15:11
 */
public class EhcacheConfig {

    public CacheManager cacheManager(){

        EhCacheManagerFactoryBean ehCacheManagerFactory = new EhCacheManagerFactoryBean();

        ehCacheManagerFactory.setConfigLocation(new ClassPathResource("EhcacheConfig/ehcache.xml"));

        ehCacheManagerFactory.afterPropertiesSet();

        return ehCacheManagerFactory.getObject();

    }

}
