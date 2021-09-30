package com.zzl.dailylearning.redissonSegmentLockDemo.service.impl;

import com.zzl.dailylearning.redissonSegmentLockDemo.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhaozl
 * @description:
 * @create: 2021-09-30 17:24
 **/
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final static String KEY_PRE = "SKU20210930";

    @Override
    public boolean sale() {

        Long waitTime = 3L;
        Long lockTime = 3L;

        String key = KEY_PRE.concat(String.valueOf(new Random().nextInt(4) + 1));
        RLock redissonClientLock = redissonClient.getLock(key);
        boolean locked1 = redissonClientLock.isLocked();
        System.out.println(locked1);
        Boolean result = false;
        try {
            boolean locked = redissonClientLock.tryLock(waitTime, lockTime, TimeUnit.SECONDS);
            Integer redisStock = 0;
            if (locked) {
                log.error(Thread.currentThread().getName() + " : 获取锁成功！");
                redisStock = Integer.valueOf(redisTemplate.opsForValue().get(key));
                if (redisStock < 1) {
                    log.error(Thread.currentThread().getName() + " : 库存已清空！");
                } else {
                    result = redisTemplate.opsForValue().setIfAbsent(key, String.valueOf(redisStock - 1));
                    if (!result) {
                        log.error(Thread.currentThread().getName() + " : 抢购失败！");
                    } else {
                        log.error(Thread.currentThread().getName() + " : 抢购成功！");
                    }
                }
            }else {
                log.error(Thread.currentThread().getName() + " : 获取锁失败！");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            redissonClientLock.unlock();
            log.info(Thread.currentThread().getName() + " : 释放锁成功！");
            return result;
        }
    }
}

