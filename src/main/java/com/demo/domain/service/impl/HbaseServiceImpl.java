package com.demo.domain.service.impl;


import com.demo.domain.service.HbaseService;
import com.google.gson.JsonArray;
import lombok.RequiredArgsConstructor;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import com.demo.domain.config.HbaseConfig;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class HbaseServiceImpl implements HbaseService{

    @Value("${spring.zookeeper.property.quorum}")
    public String zkQuorum;

    @Value("${spring.zookeeper.property.clPort}")
    public String zkClientPort;

    @Override
    public String createNamespace(String ns) throws IOException {

        HbaseConfig hbaseConfig = new HbaseConfig(zkQuorum,zkClientPort);

        Admin admin = hbaseConfig.getConnection().getAdmin();

        NamespaceDescriptor.Builder builder = NamespaceDescriptor.create(ns);
        NamespaceDescriptor build = builder.build();

        admin.createNamespace(build);

        admin.close();

        return "ns create ok!";
    }


    @Override
    public String createTable(String tbName, String colFamily) throws IOException {
        HbaseConfig hbaseConfig = new HbaseConfig(zkQuorum,zkClientPort);

        Admin admin = hbaseConfig.getConnection().getAdmin();
        //指定列族信息
        ColumnFamilyDescriptor familyDesc1 = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(colFamily))
                //在这里可以给列族设置一些属性
                .setMaxVersions(2)//指定最多存储多少个历史版本数据
                .build();

        ArrayList<ColumnFamilyDescriptor> familyList = new ArrayList<ColumnFamilyDescriptor>();
        familyList.add(familyDesc1);

        //获取TableDescriptor对象
        TableDescriptor desc = TableDescriptorBuilder.newBuilder(TableName.valueOf(tbName))//指定表名
                .setColumnFamilies(familyList)//指定列族
                .build();
        //创建表
        admin.createTable(desc);

        return "create table ok!";
    }



    public String putData(String name, String colFamily, String rowKey, List<Map<String,String>> data) throws IOException {
        HbaseConfig hbaseConfig = new HbaseConfig(zkQuorum,zkClientPort);
        Connection connection = hbaseConfig.getConnection();
        Admin admin = connection.getAdmin();
        TableName table = TableName.valueOf(name);
        if(admin.tableExists(table)) {
            Table t = connection.getTable(table);
            Put put = new Put(Bytes.toBytes(rowKey));

            for( Map<String, String> mapList : data ) {
                for( String key : mapList.keySet() ) {
                    System.out.println( key + "-->" + mapList.get(key) );
                    put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(key), Bytes.toBytes(mapList.get(key)));
                }
                t.put(put);
            }
        }else {
            System.out.println("table ["+name+"] does not exist.");
        }
        return "put data ok!";
    }


    @Override
    public String deleteDataByRowKey(String tbName,String rowKey) throws IOException {
        HbaseConfig hbaseConfig = new HbaseConfig(zkQuorum, zkClientPort);
        Connection connection = hbaseConfig.getConnection();

        Table table = connection.getTable(TableName.valueOf(tbName));
        //指定Rowkey，返回Delete对象
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        //【可选】可以在这里指定要删除指定Rowkey数据哪些列族中的列
        //delete.addColumn(Bytes.toBytes("info"),Bytes.toBytes("age"));
        table.delete(delete);
        //关闭table连接
        table.close();

        return "delete data ok!";
    }


    @Override
    public List getDataByTb(String tbName) throws IOException {

        HbaseConfig hbaseConfig = new HbaseConfig(zkQuorum, zkClientPort);
        Connection connection = hbaseConfig.getConnection();
        TableName table = TableName.valueOf(tbName);
        Table t = connection.getTable(table);
        ResultScanner rs = t.getScanner(new Scan());
        System.out.println("rs is:"+rs);
        List<List<Map>> listmap= new ArrayList<>();
        Map<Integer,List<Map>> rsMap = null;
        for (Result r : rs) {
            List<Map> listtmp= new ArrayList<>();
            System.out.println("row:" + new String(r.getRow()));
            for (Cell cell : r.rawCells()) {
                int i=0;
                Map tmpmap=new HashMap();
                tmpmap.put("colFamily",Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()));
                tmpmap.put("qualifier",Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()));
                tmpmap.put("value",Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                System.out.println("colFamily:" + Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()) + ""
                        + ",qualifier:" + Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()) +
                        ",value:" + Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                System.out.println("tmpmap:"+tmpmap);
                listtmp.add(i,tmpmap);
                i++;
            }
            System.out.println("listtmp："+listtmp);
            listmap.add(Integer.valueOf(new String(r.getRow()))-1,listtmp);
        }

        return listmap;
    }

    @Override
    public List getDataByTbRowkey(String tbName,String rowKey) throws IOException {
        HbaseConfig hbaseConfig = new HbaseConfig(zkQuorum, zkClientPort);
        Connection connection = hbaseConfig.getConnection();

        //获取Table对象，指定要操作的表名，表需要提前创建好
        Table table = connection.getTable(TableName.valueOf(tbName));
        //指定Rowkey，返回Get对象
        Get get = new Get(Bytes.toBytes(rowKey));
        Result result = table.get(get);

        System.out.println("result is:"+result);

        List<Map>listmap= new ArrayList<>();
        JsonArray jsonArray = new JsonArray();
        //如果不清楚HBase中到底有哪些列族和列，可以使用listCells()获取所有cell(单元格)，cell对应的是某一个列的数据
        List<Cell> cells = result.listCells();
        for (Cell cell: cells) {
            Map<String,String> tmpMap=new HashMap();
            //注意：下面获取的信息都是字节类型的，可以通过new String(bytes)转为字符串
            //列族
            byte[] famaily_bytes = CellUtil.cloneFamily(cell);
            //列
            byte[] column_bytes = CellUtil.cloneQualifier(cell);
            //值
            byte[] value_bytes = CellUtil.cloneValue(cell);
            System.out.println("列族："+new String(famaily_bytes)+",列："+new String(column_bytes)+",值："+new String(value_bytes));
            tmpMap.put("colFamily",new String(famaily_bytes));
            tmpMap.put("qualifier",new String(column_bytes));
            tmpMap.put("value",new String(value_bytes));
            listmap.add(tmpMap);
        }

        return listmap;
    }
}
