package com.pavelkostal.api.config;

import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class RedissonCacheConfig {
    
    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();
        // create "photoGame:photos" cache with ttl = 30 minutes and maxIdleTime = 15 minutes
        config.put("photoGame:photos", new CacheConfig(30*60*1000, 15*60*1000));
        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
