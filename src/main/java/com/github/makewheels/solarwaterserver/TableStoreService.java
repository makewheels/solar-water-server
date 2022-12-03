package com.github.makewheels.solarwaterserver;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.*;
import com.github.makewheels.solarwaterserver.util.SnowFlakeIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.jeasy.random.EasyRandom;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class TableStoreService {
    private final String endPoint = "https://instance-test.cn-beijing.ots.aliyuncs.com";
    private final String accessKeyId = Base64.decodeStr("TFRBSTV0Q29zaUY0cFQ5WjhUYUx5QXIy");
    private final String accessKeySecret = Base64.decodeStr(
            "WFR2eTVNTjB2TzNxUFBjbTF4V3U4dGhvYTR5eXYw");
    private final String instanceName = "solor";
    private final SyncClient tableStoreClient = new SyncClient(endPoint, accessKeyId, accessKeySecret,
            instanceName);

    private PrimaryKey getPrimaryKey(String id) {
        return PrimaryKeyBuilder.createPrimaryKeyBuilder()
                .addPrimaryKeyColumn("id", PrimaryKeyValue.fromString(id))
                .build();
    }

    public void insert(String tableName, Object object) {
        String id = SnowFlakeIdUtil.get();
        PrimaryKey primaryKey = getPrimaryKey(id);

        Map<String, Object> map = null;
        try {
            map = PropertyUtils.describe(object);
            PropertyUtils.setProperty(object, "id", id);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        //设置数据表名称
        RowPutChange rowPutChange = new RowPutChange(tableName, primaryKey);
        //加入属性列
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            if (key.equals("id")) continue;
            ColumnValue columnValue = null;
            Object value = map.get(key);
            if (value instanceof String) {
                columnValue = ColumnValue.fromString((String) value);
            } else if (value instanceof Long || value instanceof Integer) {
                columnValue = ColumnValue.fromLong((Long) value);
            } else if (value instanceof Boolean) {
                columnValue = ColumnValue.fromBoolean((Boolean) value);
            } else if (value instanceof Byte[]) {
                columnValue = ColumnValue.fromBinary((byte[]) value);
            }
            rowPutChange.addColumn(new Column(key, columnValue));
        }

        log.info("插入TableStore: object = {}", JSON.toJSONString(object));

        PutRowRequest putRowRequest = new PutRowRequest(rowPutChange);
        PutRowResponse putRowResponse = tableStoreClient.putRow(putRowRequest);
        log.info("插入TableStore: putRowResponse = {}", JSON.toJSONString(putRowResponse));
        tableStoreClient.shutdown();
    }

    public static void main(String[] args) {
        EasyRandom easyRandom = new EasyRandom();
        IotConnectDTO iotConnectDTO = easyRandom.nextObject(IotConnectDTO.class);
        new TableStoreService().insert("test", iotConnectDTO);
    }
}
