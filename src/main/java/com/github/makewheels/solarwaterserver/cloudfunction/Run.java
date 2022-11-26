package com.github.makewheels.solarwaterserver.cloudfunction;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.StreamRequestHandler;
import com.github.makewheels.solarwaterserver.WaterService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Run implements StreamRequestHandler {
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        int connect_time_length_in_seconds = Integer.parseInt(
                System.getenv("connect_time_length_in_seconds"));
        new WaterService().connect(connect_time_length_in_seconds * 1000L);
    }
}
