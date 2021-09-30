package com.zzl.dailylearning.redissonSegmentLockDemo.controller;


import com.zzl.dailylearning.redissonSegmentLockDemo.service.impl.OrderServiceImpl;
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
public class RedissonSegmentDemoController {

    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping("/redissonSegmentLockTest")
    public String redissonSegmentLockTest() {
        boolean save = orderService.sale();
        if (save) {
            return "秒杀成功!";
        }
        return "秒杀失败！";
    }

}
