package com.github.makewheels.solarwaterserver.util;

import cn.hutool.core.util.IdUtil;

public class SnowFlakeIdUtil {
    public static String get(){
        //1599030718854172672
        //C5CP2Z6O2PZ5
        long snowflake = IdUtil.getSnowflakeNextId();
        return Long.toString(snowflake,36).toUpperCase();
    }

}
