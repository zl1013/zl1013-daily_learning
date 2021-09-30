//package com.zzl.dailylearning.redissonLockDemo.config;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author: zhaozl
// * @description:
// * @create: 2021-09-30 09:09
// **/
//@Configuration
//public class RedissonConfig {
//
//    @Bean
//    public RedissonClient getRedissonClient() {
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
//        return Redisson.create(config);
//    }
//}
