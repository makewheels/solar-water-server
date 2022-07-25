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
                .setAccessKeyId(Base64.decodeStr("TFRBSTV0RDZIajRpNkQ4Q3h6Qmh1Zktr"))
                .setAccessKeySecret(Base64.decodeStr(
                        "d2NaMXIwNVhpc2NZSER4cGo5ZklScjN4eFVIcEFX"));
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

        System.out.println(JSON.toJSONString(pubResponse));
    }


    public String connect(long timeLength) {
        doAliyunConnect(timeLength + "");
        log.info("链接: " + timeLength);
        return "response-" + System.currentTimeMillis();
    }

}