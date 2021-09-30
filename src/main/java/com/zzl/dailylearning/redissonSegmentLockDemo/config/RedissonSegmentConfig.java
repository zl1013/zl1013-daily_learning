package com.zzl.dailylearning.redissonSegmentLockDemo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zhaozl
 * @description:
 * @create: 2021-09-30 09:09
 **/
@Configuration
public class RedissonSegmentConfig {

    @Bean
    public RedissonClient getRedissonSegment() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }
}
