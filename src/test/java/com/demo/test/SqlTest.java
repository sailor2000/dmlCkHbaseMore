package com.demo.test;

import com.demo.App;
import com.demo.domain.config.ClickHouseConfig;
import com.demo.domain.config.HbaseConfig;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SqlTest {

    @Value("${spring.zookeeper.property.quorum}")
    public String zkQuorum;

    @Value("${spring.zookeeper.property.clPort}")
    public String zkClientPort;

    @Test
    public void select_Test() {
        String sql="select * from order";
        List<Map<String,String>> result= ClickHouseConfig.exeSql(sql);

        System.out.println(result);
    }

    @Test
    public void createTable() throws IOException {
        HbaseConfig hbaseConfig = new HbaseConfig(zkQuorum,zkClientPort);

        Admin admin = hbaseConfig.getConnection().getAdmin();
        //指定列族信息
        ColumnFamilyDescriptor familyDesc1 = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("info"))
                //在这里可以给列族设置一些属性
                .setMaxVersions(3)//指定最多存储多少个历史版本数据
                .build();
        ColumnFamilyDescriptor familyDesc2 = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("level"))
                //在这里可以给列族设置一些属性
                .setMaxVersions(2)//指定最多存储多少个历史版本数据
                .build();
        ArrayList<ColumnFamilyDescriptor> familyList = new ArrayList<ColumnFamilyDescriptor>();
        familyList.add(familyDesc1);
        familyList.add(familyDesc2);

        //获取TableDescriptor对象
        TableDescriptor desc = TableDescriptorBuilder.newBuilder(TableName.valueOf("test"))//指定表名
                .setColumnFamilies(familyList)//指定列族
                .build();
        //创建表
        admin.createTable(desc);

    }

    @Test
    public void putData() throws IOException {
        HbaseConfig hbaseConfig = new HbaseConfig(zkQuorum,zkClientPort);
        Connection connection = hbaseConfig.getConnection();
        Admin admin = connection.getAdmin();
        TableName table = TableName.valueOf("test");
        if(admin.tableExists(table)) {
            Table t = connection.getTable(table);
            Put put = new Put(Bytes.toBytes("3"));
            Map<String,String> data =new HashMap();
            data.put("33","a");
            data.put("44","b");

            for(Map.Entry<String, String> entry:data.entrySet()) {
                put.addColumn(Bytes.toBytes("info"), Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
            }
            t.put(put);
        }else {
            System.out.println("table tbtb does not exist.");
        }

    }

}
