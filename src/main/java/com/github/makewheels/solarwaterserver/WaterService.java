package com.github.makewheels.solarwaterserver;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.fc.runtime.Context;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.github.makewheels.solarwaterserver.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class WaterService {
    private final String productKey = "a1g0aXwTsx1";
    private final String deviceName = "ZApjdvupCbBZlbJYFILD";

    private String tableStoreEndPoint = "https://solor.cn-beijing.vpc.tablestore.aliyuncs.com";
    private String tableStoreTableName = "connect_log";

    private Client iotClient;

    private IotConnectDTO iotConnectDTO = new IotConnectDTO();

    private Client getIotClient() {
        if (iotClient != null) {
            return iotClient;
        }
        Config config = new Config()
                .setAccessKeyId(System.getenv("solor_iot_AccessKeyID"))
                .setAccessKeySecret(System.getenv("solor_iot_AccessKeySecret"))
                .setEndpoint("iot.cn-shanghai.aliyuncs.com");
        try {
            iotClient = new Client(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iotClient;
    }

    /**
     * 获取设备在线状态
     * ONLINE：设备在线。
     * OFFLINE：设备离线。
     * UNACTIVE：设备未激活。
     * DISABLE：设备已禁用。
     */
    private GetDeviceStatusResponse getDeviceStatus() {
        GetDeviceStatusRequest request = new GetDeviceStatusRequest();
        request.setProductKey(productKey);
        request.setDeviceName(deviceName);
        try {
            return getIotClient().getDeviceStatus(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送连接指令
     */
    private void doIotConnect(String content) {
        PubRequest request = new PubRequest()
                .setProductKey(productKey)
                .setTopicFullName("/" + productKey + "/" + deviceName + "/user/get")
                .setMessageContent(Base64.encode(content));

        PubResponse response = null;
        try {
            response = getIotClient().pub(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PubResponseBody body = response.getBody();
        iotConnectDTO.setIotMessageId(body.getMessageId());
        iotConnectDTO.setIotRequestId(body.getRequestId());
        log.info("阿里云iot返回:");
        log.info(JSON.toJSONString(response));
    }

    public String connect(Context context, long timeLengthInMillis) {
        if (context != null) {
            iotConnectDTO.setCfRequestId(context.getRequestId());
            iotConnectDTO.setCfContextService(JSONUtil.toJSONObject(context.getService()));
            iotConnectDTO.setCfContextFunctionParam(JSONUtil.toJSONObject(context.getFunctionParam()));
        }
        iotConnectDTO.setConnectTimeInMillis(timeLengthInMillis);

        //获取设备在线状态
        GetDeviceStatusResponse deviceStatusResponse = getDeviceStatus();
        String deviceStatus = deviceStatusResponse.getBody().getData().getStatus();
        Long deviceTimestamp = deviceStatusResponse.getBody().getData().getTimestamp();

        iotConnectDTO.setIotDeviceStatus(deviceStatus);
        iotConnectDTO.setIotDeviceStatusResponse(JSONUtil.toJSONObject(deviceStatusResponse));
        iotConnectDTO.setIotDeviceStatusChangeTimestamp(deviceTimestamp);

        log.info("设备当前在线状态：{}", deviceStatus);
        log.info("设备状态变更时间：{}", DateUtil.formatDateTime(new Date(deviceTimestamp)));

        //如果设备在线，才发送命令
        if (deviceStatus.equals("ONLINE")) {
            log.info("设备在线，开始链接: " + timeLengthInMillis + " ms");
            doIotConnect(timeLengthInMillis + "");
        } else {
            log.info("设备离线，不发送指令");
        }

        TableStoreService tableStoreService = new TableStoreService(tableStoreEndPoint);
        tableStoreService.insert(tableStoreTableName, iotConnectDTO);
        return "response-" + System.currentTimeMillis();
    }

}