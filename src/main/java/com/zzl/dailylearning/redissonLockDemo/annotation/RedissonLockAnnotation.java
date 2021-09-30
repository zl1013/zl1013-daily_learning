package com.zzl.dailylearning.redissonLockDemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author zhaozl
 * @description
 * @date ate 2021/9/29
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLockAnnotation {
    /**
     * 加锁的key的前缀
     * @return
     */
    String lockPrefix() default "";

    /**
     * 加锁的key值
     * @return
     */
    String lockKey();

    /**
     * 加锁的key的后缀
     * @return
     */
    String lockSuffix() default "";

    /**
     * 超时时间（释放锁时间），单位S
     * @return
     */
    int lockTime() default 5;


    /**
     * 获取锁的最大等待时间
     * @return
     */
    int waitTime() default 0;

    /**
     * 获取锁失败提示
     * @return
     */
    String failMessage() default "前方拥挤，请稍后再试";

    /**
     * 未获取到锁是否提示消息
     * @return
     */
    boolean remindFailMessage() default true;
}
