package com.demo.domain.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Accessors(chain = true)
@EqualsAndHashCode()
@Data
public class HbaseReq implements Serializable {

    private String tbname;
    private String colFamily;
    private String rowKey;
    private List<Map<String,String>> data;

}
