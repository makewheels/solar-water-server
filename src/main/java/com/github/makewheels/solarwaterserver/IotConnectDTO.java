package com.github.makewheels.solarwaterserver;

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
    private String iotDeviceStatusJson;

    private String cfRequestId;
    private String cfContextFunctionParamJson;
    private String cfContextServiceJson;

}
