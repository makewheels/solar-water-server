package com.github.makewheels.solarwaterserver;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

@Data
public class IotConnectDTO {
    private String id;
    private Date createTime;
    private String createTimeString;

    private Long connectTimeInMillis;

    private String iotMessageId;
    private String iotRequestId;
    private String iotDeviceStatus;
    private Date iotDeviceStatusChangeTime;
    private String iotDeviceStatusChangeTimeString;
    private JSONObject iotDeviceStatusResponse;

    private String cfRequestId;
    private JSONObject cfContextFunctionParam;
    private JSONObject cfContextService;

}
