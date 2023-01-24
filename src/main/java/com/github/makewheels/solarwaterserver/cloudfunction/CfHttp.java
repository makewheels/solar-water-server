package com.github.makewheels.solarwaterserver.cloudfunction;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.HttpRequestHandler;
import com.aliyun.fc.runtime.StreamRequestHandler;
import com.github.makewheels.solarwaterserver.WaterService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CfHttp implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response, Context context) {
        long connectTimeLengthInMilliSeconds = Long.parseLong(
                request.getParameter("connectTimeLengthInMilliSeconds"));
        new WaterService().connect(context, connectTimeLengthInMilliSeconds);

    }
}
