package com.zhurzh.nodeorderservice.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


@Configuration
public class MyConfiguration {

//    @Bean
//    public CacheManager cacheManager() {
//        return new EhCacheCacheManager(ehCacheManagerFactory().getObject());
//    }
//
//    @Bean
//    public EhCacheManagerFactoryBean ehCacheManagerFactory() {
//        EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
//        factory.setConfigLocation(new ClassPathResource("resources/ehcache.xml")); // Укажите путь к вашему файлу ehcache.xml
//        factory.setShared(true);
//        return factory;
//    }
}
