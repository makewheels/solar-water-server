package com.github.makewheels.solarwaterserver.cloudfunction;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.HttpRequestHandler;
import com.aliyun.iot20180120.models.GetDeviceStatusResponseBody;
import com.github.makewheels.solarwaterserver.WaterService;
import com.github.makewheels.solarwaterserver.response.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CfHttpGetDeviceStatus implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response, Context context)
            throws IOException {
        GetDeviceStatusResponseBody.GetDeviceStatusResponseBodyData data
                = new WaterService().getDeviceStatus().getBody().getData();
        String status = data.getStatus();
        Long timestamp = data.getTimestamp();

        JSONObject responseBody = new JSONObject();
        responseBody.put("deviceStatus", status);
        responseBody.put("timestamp", timestamp);

        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(Result.ok(responseBody)));
        writer.close();
    }
}
