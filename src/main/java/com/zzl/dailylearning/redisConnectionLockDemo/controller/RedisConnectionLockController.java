package com.zzl.dailylearning.redisConnectionLockDemo.controller;

import com.zzl.dailylearning.redisConnectionLockDemo.annotation.RedisConnectionLockAnnotation;
import com.zzl.dailylearning.redisConnectionLockDemo.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: huoli
 * @create: 2021-09-29 09:48
 **/
@RestController
@RequestMapping("/api")
public class RedisConnectionLockController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/test")
    @RedisConnectionLockAnnotation(lockKey = "lockKeyFromUrl", lockPrefix = "pre", lockSuffix = "suf", lockTime = 5, waitTime = 10)
    public Boolean test() {
        boolean save = userService.save();
        return save;
    }
}
