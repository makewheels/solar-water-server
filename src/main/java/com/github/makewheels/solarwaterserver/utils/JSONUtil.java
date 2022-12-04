package com.github.makewheels.solarwaterserver.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JSONUtil {
    public static JSONObject toJSONObject(Object object) {
        return JSONObject.parseObject(JSON.toJSONString(object));
    }
}
