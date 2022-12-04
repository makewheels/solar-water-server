package com.github.makewheels.solarwaterserver;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class IotConnectDTO {
    private String id;
    private Long createTimestamp;
    private String createTimeString;

    private Long connectTimeInMillis;

    private String iotMessageId;
    private String iotRequestId;
    private String iotDeviceStatus;
    private Long iotDeviceStatusChangeTimestamp;
    private JSONObject iotDeviceStatusResponse;

    private String cfRequestId;
    private JSONObject cfContextFunctionParam;
    private JSONObject cfContextService;

}
