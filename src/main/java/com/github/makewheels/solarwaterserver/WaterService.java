package com.github.makewheels.solarwaterserver;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.PubRequest;
import com.aliyun.iot20180120.models.PubResponse;
import com.aliyun.teaopenapi.models.Config;

public class WaterService {

    public static Client createClient(String accessKeyId, String accessKeySecret)
            throws Exception {
        Config config = new Config()
                // 您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        config.endpoint = "iot.cn-shanghai.aliyuncs.com";
        return new Client(config);
    }

    public static void main(String[] args) {
        Client client;
        try {
            client = WaterService.createClient(
                    Base64.decodeStr("TFRBSTV0RDZIajRpNkQ4Q3h6Qmh1Zktr"),
                    Base64.decodeStr("d2NaMXIwNVhpc2NZSER4cGo5ZklScjN4eFVIcEFX"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String content = "2000";
        PubRequest pubRequest = new PubRequest()
                .setProductKey("a1g0aXwTsx1")
                .setTopicFullName("/a1g0aXwTsx1/ZApjdvupCbBZlbJYFILD/user/get")
                .setMessageContent(Base64.encode(content));
        PubResponse pubResponse;
        try {
            pubResponse = client.pub(pubRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(JSON.toJSONString(pubResponse));
    }
}