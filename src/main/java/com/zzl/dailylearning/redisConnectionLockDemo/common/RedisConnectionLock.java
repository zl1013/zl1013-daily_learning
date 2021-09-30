package com.zzl.dailylearning.redisConnectionLockDemo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: huoli
 * @create: 2021-09-29 14:09
 **/
@Component
@Slf4j
public class RedisConnectionLock {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * redisTemplate操作redisConnection实现setnx和setex两个命令连用
     * 获取锁
     *
     * @param key    锁的Key
     * @param value  锁的内容
     * @param expire 锁失效时间
     */
    public Boolean lock(String key, String value, long expire) {
        log.info(Thread.currentThread().getName() + " : {} ", value);
        try {
            return redisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.set(key.getBytes(), value.getBytes(), Expiration.seconds(expire), RedisStringCommands.SetOption.ifAbsent());
                }
            });
        } catch (Exception e) {
            log.error("set redis occured an exception", e);
        }
        return false;
    }


    /**
     * 释放锁操作
     *
     * @param key   锁的Key
     * @param value 锁里面的值
     */
    public Boolean unlock(String key, String value) {
        if (value.equals(redisTemplate.opsForValue().get(key))) {
            return redisTemplate.delete(key);
        } else {
            return false;
        }
    }
}
