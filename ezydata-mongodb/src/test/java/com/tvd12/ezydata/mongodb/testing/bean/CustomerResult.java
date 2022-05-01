package com.tvd12.ezydata.mongodb.testing.bean;

import lombok.Data;

@Data
public class CustomerResult {
    private String id;
    private String name;
    private String hello = "hello";
    private String world = "world";
}
