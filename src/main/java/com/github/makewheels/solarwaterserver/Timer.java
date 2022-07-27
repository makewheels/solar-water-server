package com.github.makewheels.solarwaterserver;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Timer {
    @Resource
    private WaterService waterService;

    @Scheduled(cron = "0 0 4 * * ?")
    public void task1() {
        waterService.connect(1000 * 60 * 10);
    }

    @Scheduled(cron = "0 2 12 * * ?")
    public void task2() {
        waterService.connect(1000 * 60 * 5);
    }

}
