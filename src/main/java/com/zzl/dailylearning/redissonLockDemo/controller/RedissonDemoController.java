package com.zzl.dailylearning.redissonLockDemo.controller;


import com.zzl.dailylearning.redisConnectionLockDemo.service.impl.UserServiceImpl;
import com.zzl.dailylearning.redissonLockDemo.annotation.RedissonLockAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RedissonDemoController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/redissonTest")
    @RedissonLockAnnotation(lockKey = "lockKeyFromUrl", lockPrefix = "pre", lockSuffix = "suf", lockTime = 2, waitTime = 1)
    public String redissonTest() {

        boolean save = userService.save();
        if (save) {
            return "Success!";
        }
        return "filed";
    }

    @GetMapping("/redissonTestMore")
    @RedissonLockAnnotation(lockKey = "lockKeyFromUrl", lockPrefix = "pre", lockSuffix = "suf", lockTime = 100, waitTime = 100)
    public String redissonTestMore() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean save = userService.save();
        if (save) {
            return "Success!";
        }
        return "filed";
    }

}
