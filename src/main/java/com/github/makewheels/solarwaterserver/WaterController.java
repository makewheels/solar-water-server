package com.github.makewheels.solarwaterserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class WaterController {
    @Resource
    private WaterService waterService;

    @GetMapping("connect")
    public String connect(@RequestParam long timeLength) {
        return waterService.connect(null, timeLength);
    }
}
