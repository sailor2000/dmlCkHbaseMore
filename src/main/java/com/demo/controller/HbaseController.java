package com.demo.controller;

import com.demo.domain.entity.HbaseReq;
import com.demo.domain.service.HbaseService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/hbaseDmlApi")
public class HbaseController {

    private final HbaseService hbaseService;

    public HbaseController(HbaseService hbaseService) {
        this.hbaseService = hbaseService;
    }

    @RequestMapping(value = "/putData",method = RequestMethod.POST)
    public String putData(@RequestBody HbaseReq hbaseReq) throws IOException {
        String tbname;
        String colFamily;
        String rowKey;
        if(!hbaseReq.getTbname().isEmpty()){
            tbname=hbaseReq.getTbname();
        }else{
            return  "error,reqParam must exist key 'tbname' ";
        }
        if(!hbaseReq.getColFamily().isEmpty()){
            colFamily=hbaseReq.getColFamily();
        }else{
            return  "error,reqParam must exist key 'colFamily' ";
        }
        if(!hbaseReq.getRowKey().isEmpty()){
            rowKey=hbaseReq.getRowKey();
        }else{
            return  "error,reqParam must exist key 'rowKey' ";
        }

        String rs = hbaseService.putData(tbname,colFamily,rowKey,hbaseReq.getData());
        return rs;
    }


    @RequestMapping(value = "/getDataByTb",method = RequestMethod.GET)
    public List getData(String tbname) throws IOException {

        return hbaseService.getDataByTb(tbname);
    }

    @RequestMapping(value = "/getDataByTbRowkey",method = RequestMethod.GET)
    public List getDataByTbRowkey(String tbname, String rowKey) throws IOException {

         return hbaseService.getDataByTbRowkey(tbname,rowKey);
    }

    @RequestMapping(value = "/createNamespace",method = RequestMethod.GET)
    public String createNamespace(String ns) throws IOException {

        String rs = hbaseService.createNamespace(ns);
        return rs;
    }

    @RequestMapping(value = "/createTable",method = RequestMethod.GET)
    public String createTable(String tbname,String colFamily) throws IOException {

        String rs = hbaseService.createTable(tbname,colFamily);
        return rs;
    }
    @RequestMapping(value = "/deleteDataByRowKey",method = RequestMethod.GET)
    public String deleteDataByRowKey(String tbname,String rowKey) throws IOException {

        String rs = hbaseService.deleteDataByRowKey(tbname,rowKey);
        return rs;
    }
}
