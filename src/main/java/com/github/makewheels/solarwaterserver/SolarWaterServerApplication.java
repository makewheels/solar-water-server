package com.github.makewheels.solarwaterserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SolarWaterServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolarWaterServerApplication.class, args);
    }

}
