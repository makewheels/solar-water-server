package com.github.makewheels.solarwaterserver;

import lombok.Data;

@Data
public class IotConnectDTO {
    private String id;
    private Long createTimestamp;
    private Long connectTimeInMillis;

    private String iotMessageId;
    private String iotRequestId;

    private String iotRequestJson;
    private String iotResponseJson;

    private String cfRequestId;
    private String cfFunctionParamJson;
    private String cfServiceJson;

}
