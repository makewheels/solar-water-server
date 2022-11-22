package com.github.makewheels.solarwaterserver;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Timer {
    @Resource
    private WaterService waterService;

    //夏天定时上水1
//    @Scheduled(cron = "0 0 4 * * ?")
//    public void task1() {
//        waterService.connect(1000 * 60 * 10);
//    }

    //夏天定时上水2
//    @Scheduled(cron = "0 2 12 * * ?")
//    public void task2() {
//        waterService.connect(1000 * 60 * 5);
//    }

    //冬天防冻顶水
    @Scheduled(cron = "0 3,33 21-23,0-6 * * ?")
    public void task3() {
        waterService.connect(1000 * 15);
    }

}
