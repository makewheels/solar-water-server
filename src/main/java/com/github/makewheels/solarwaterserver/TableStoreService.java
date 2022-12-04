package com.github.makewheels.solarwaterserver;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;
import java.util.Set;

@Slf4j
@NoArgsConstructor
public class TableStoreService {
    private String endPoint = "https://instance-test.cn-beijing.ots.aliyuncs.com";
    private String accessKeyId = Base64.decodeStr("TFRBSTV0Q29zaUY0cFQ5WjhUYUx5QXIy");
    private String accessKeySecret = Base64.decodeStr(
            "WFR2eTVNTjB2TzNxUFBjbTF4V3U4dGhvYTR5eXYw");
    private String instanceName = "solor";
    private SyncClient tableStoreClient;

    public TableStoreService(String endPoint) {
        this.endPoint = endPoint;
        tableStoreClient = new SyncClient(endPoint, accessKeyId, accessKeySecret, instanceName);
    }

    private PrimaryKey getPrimaryKey(String id) {
        return PrimaryKeyBuilder.createPrimaryKeyBuilder()
                .addPrimaryKeyColumn("id", PrimaryKeyValue.fromString(id))
                .build();
    }

    private String getId() {
        //1599030718854172672
        //C5CP2Z6O2PZ5
        long snowflake = IdUtil.getSnowflakeNextId();
        return Long.toString(snowflake, 36).toUpperCase();
    }

    public void insert(String tableName, Object object) {
        String id = getId();
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
        rowPutChange.addColumn(new Column("createTimestamp", ColumnValue.fromLong(
                System.currentTimeMillis())));
        rowPutChange.addColumn(new Column("createTimeString", ColumnValue.fromString(
                DateUtil.formatDateTime(new Date()))));

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
//        EasyRandom easyRandom = new EasyRandom();
//        IotConnectDTO iotConnectDTO = easyRandom.nextObject(IotConnectDTO.class);
//        new TableStoreService().insert("test", iotConnectDTO);
    }
}
