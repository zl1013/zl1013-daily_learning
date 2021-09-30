package com.zzl.dailylearning.redisConnectionLockDemo.aop;

import com.zzl.dailylearning.redisConnectionLockDemo.annotation.RedisConnectionLockAnnotation;
import com.zzl.dailylearning.redisConnectionLockDemo.common.RedisConnectionLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @description:
 * @author: huoli
 * @create: 2021-09-29 13:29
 **/
@Aspect
@Component
@Slf4j
public class LockAspect {

    @Autowired
    private RedisConnectionLock redisLock;

    @Around("@annotation(lockAnnotation)")
    private Object lockAround(ProceedingJoinPoint joinPoint, RedisConnectionLockAnnotation lockAnnotation) {
        String lockKey = lockAnnotation.lockKey();
        String lockPrefix = lockAnnotation.lockPrefix();
        String lockSuffix = lockAnnotation.lockSuffix();
        Long lockTime = Long.valueOf(lockAnnotation.lockTime());

        String value = UUID.randomUUID().toString().substring(0, 5);
        String key = lockPrefix.concat(lockKey).concat(lockSuffix);
        Boolean status = false;
        Boolean lock = null;
        if (!status) {
            for (int i = 0; i < 20; i++) {
                lock = redisLock.lock(key, value, lockTime);
                if (lock) {
                    status = true;
                    break;
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!status) {
                log.error(Thread.currentThread().getName() + " : 获取锁超时！");
                return false;
            }
        }
        if (lock) {
            log.info(Thread.currentThread().getName() + " : 获取锁成功！");
        }
        try {
            Object proceed = joinPoint.proceed();
            log.info(Thread.currentThread().getName() + " : 业务执行完毕");
            return proceed;
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("业务处理异常：{}", e);
        } finally {
            Boolean unlock = redisLock.unlock(key, value);
            if (unlock) {
                log.info(Thread.currentThread().getName() + " : 释放锁成功");
            } else {
                log.info(Thread.currentThread().getName() + " : 释放锁失败");
            }
        }
        return null;
    }
}
