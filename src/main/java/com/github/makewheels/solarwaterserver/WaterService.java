package com.github.makewheels.solarwaterserver;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.aliyun.fc.runtime.Context;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.PubRequest;
import com.aliyun.iot20180120.models.PubResponse;
import com.aliyun.iot20180120.models.PubResponseBody;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WaterService {
    private Client aliyunIotClient;

    private IotConnectDTO iotConnectDTO = new IotConnectDTO();

    private Client getClient() throws Exception {
        if (aliyunIotClient != null) {
            return aliyunIotClient;
        }
        Config config = new Config()
                .setAccessKeyId(System.getenv("solor_iot_AccessKeyID"))
                .setAccessKeySecret(System.getenv("solor_iot_AccessKeySecret"))
                .setEndpoint("iot.cn-shanghai.aliyuncs.com");
        aliyunIotClient = new Client(config);
        return aliyunIotClient;
    }

    private void doAliyunConnect(String content) {
        PubRequest request = new PubRequest()
                .setProductKey("a1g0aXwTsx1")
                .setTopicFullName("/a1g0aXwTsx1/ZApjdvupCbBZlbJYFILD/user/get")
                .setMessageContent(Base64.encode(content));
        iotConnectDTO.setIotRequestJson(JSON.toJSONString(content));

        PubResponse response = null;
        try {
            response = getClient().pub(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PubResponseBody body = response.getBody();
        iotConnectDTO.setIotMessageId(body.getMessageId());
        iotConnectDTO.setIotRequestId(body.getRequestId());
        iotConnectDTO.setIotResponseJson(JSON.toJSONString(response));
        log.info("阿里云iot返回:");
        log.info(JSON.toJSONString(response));
    }

    public String connect(Context context, long timeLengthInMillis) {
        if (context != null) {
            iotConnectDTO.setCfRequestId(context.getRequestId());
            iotConnectDTO.setCfServiceJson(JSON.toJSONString(context.getService()));
            iotConnectDTO.setCfFunctionParamJson(JSON.toJSONString(context.getFunctionParam()));
        }

        iotConnectDTO.setConnectTimeInMillis(timeLengthInMillis);
        log.info("链接: " + timeLengthInMillis + " ms");

        doAliyunConnect(timeLengthInMillis + "");

        iotConnectDTO.setCreateTimestamp(System.currentTimeMillis());

        String endPoint = "https://solor.cn-beijing.vpc.tablestore.aliyuncs.com";
        TableStoreService tableStoreService = new TableStoreService(endPoint);
        tableStoreService.insert("connect_log", iotConnectDTO);
        return "response-" + System.currentTimeMillis();
    }

}