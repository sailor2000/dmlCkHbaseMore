package com.demo.domain.config;


import java.io.IOException;

import lombok.RequiredArgsConstructor;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.apache.hadoop.conf.Configuration;
import org.springframework.stereotype.Component;

@Component
public class HbaseConfig {

    private String zkQuorum;

    private String zkClientPort;

    public HbaseConfig(){

    }

    public HbaseConfig(String zkQuorum, String zkClientPort){
        this.zkQuorum=zkQuorum;
        this.zkClientPort=zkClientPort;
    }


    public Connection getConnection() throws IOException{
        Configuration config = null;
        Connection conn = null;
        System.out.println("zkQuorum:"+this.zkQuorum);
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", zkQuorum);
        config.set("hbase.zookeeper.property.clientPort", zkClientPort);
        conn = ConnectionFactory.createConnection(config);
        return conn;
    }

}