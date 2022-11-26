package com.github.makewheels.solarwaterserver;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson2.JSON;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.PubRequest;
import com.aliyun.iot20180120.models.PubResponse;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WaterService {
    private Client aliyunClient;

    private Client getClient() throws Exception {
        if (aliyunClient != null) {
            return aliyunClient;
        }
        Config config = new Config()
                .setAccessKeyId(System.getenv("solor_iot_AccessKeyID"))
                .setAccessKeySecret(System.getenv("solor_iot_AccessKeySecret"));
        config.endpoint = "iot.cn-shanghai.aliyuncs.com";
        aliyunClient = new Client(config);
        return aliyunClient;
    }

    private void doAliyunConnect(String content) {
        PubRequest pubRequest = new PubRequest()
                .setProductKey("a1g0aXwTsx1")
                .setTopicFullName("/a1g0aXwTsx1/ZApjdvupCbBZlbJYFILD/user/get")
                .setMessageContent(Base64.encode(content));
        PubResponse pubResponse;
        try {
            pubResponse = getClient().pub(pubRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info(JSON.toJSONString(pubResponse));
    }

    public String connect(long timeLength) {
        log.info("链接: " + timeLength);
        doAliyunConnect(timeLength + "");
        return "response-" + System.currentTimeMillis();
    }

}