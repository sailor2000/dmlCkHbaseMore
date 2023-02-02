package com.demo.domain.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service
public interface HbaseService {

    List getDataByTb(String tbName) throws IOException;
    List getDataByTbRowkey(String tbName, String rowKey) throws IOException;
    String putData(String tbName, String colFamily, String rowKey, List<Map<String,String>> data) throws IOException;
    String deleteDataByRowKey(String tbName,String rowKey) throws IOException;
    String createNamespace(String ns) throws IOException;
    String createTable(String tbName,String colFamily) throws IOException;


}
