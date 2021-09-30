package com.zzl.dailylearning.redissonSegmentLockDemo.aop;

import com.zzl.dailylearning.redissonLockDemo.annotation.RedissonLockAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: huoli
 * @create: 2021-09-29 13:29
 **/
@Aspect
@Component
@Slf4j
public class RedissonSegmentLockAspect {

    @Autowired
    private RedissonClient redissonClient;

    @Around("@annotation(lockAnnotation)")
    private Object lockAround(ProceedingJoinPoint joinPoint, RedissonLockAnnotation lockAnnotation) {
        String lockKey = lockAnnotation.lockKey();
        String lockPrefix = lockAnnotation.lockPrefix();
        String lockSuffix = lockAnnotation.lockSuffix();

        Long lockTime = Long.valueOf(lockAnnotation.lockTime());
        Long waitTime = Long.valueOf(lockAnnotation.waitTime());

        boolean remindFailMessage = lockAnnotation.remindFailMessage();
        String failMessage = lockAnnotation.failMessage();


        RLock clientLock = redissonClient.getLock(lockPrefix.concat(lockKey).concat(lockSuffix));
        try {
            boolean locked = clientLock.tryLock(waitTime, lockTime, TimeUnit.SECONDS);
            if (locked) {
                log.info(Thread.currentThread().getName() + " : 获取锁成功！");
                log.info(Thread.currentThread().getName() + " : 开始处理业务！");
                Object proceed = joinPoint.proceed();
                log.info(Thread.currentThread().getName() + " : 业务处理完成！");
                return proceed;
            } else {
                log.info(Thread.currentThread().getName() + " : 获取锁失败！");
                if (remindFailMessage) {
                    return failMessage;
                } else {
                    return null;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } catch (Throwable throwable) {
            log.error("业务处理异常，信息：{}", throwable.getMessage());
            return null;
        } finally {
            if (clientLock.isHeldByCurrentThread()) {
                clientLock.unlock();
            }
        }
    }
}
