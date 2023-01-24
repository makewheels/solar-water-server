package com.github.makewheels.solarwaterserver.cloudfunction;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.StreamRequestHandler;
import com.github.makewheels.solarwaterserver.WaterService;

import java.io.InputStream;
import java.io.OutputStream;

public class CfConnect implements StreamRequestHandler {
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) {
        long connectTimeLengthInSeconds = Long.parseLong(
                System.getenv("connect_time_length_in_seconds"));
        new WaterService().connect(context,connectTimeLengthInSeconds * 1000);
    }
}
