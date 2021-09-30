package com.zzl.dailylearning.redisConnectionLockDemo.service.impl;

import com.zzl.dailylearning.redisConnectionLockDemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @description:
 * @author: huoli
 * @create: 2021-09-29 16:30
 **/
@Component
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean save() {
        String value = UUID.randomUUID().toString();
        log.info(Thread.currentThread().getName() + " : addUser");
        redisTemplate.opsForValue().set(Thread.currentThread().getName(), value);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (value.equals(redisTemplate.opsForValue().get(Thread.currentThread().getName()))) {
            return true;
        } else {
            return false;
        }
    }
}
