package com.demo.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class Order implements Serializable {
    private Long Id;
    private Long userId;
    private String title;
    private Float price;
    private Date createTime;
    private Date updateTime;
}
