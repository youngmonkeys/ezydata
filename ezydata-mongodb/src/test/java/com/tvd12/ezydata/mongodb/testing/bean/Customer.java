package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EzyCollection("test_mongo_bean_customer")
public class Customer {

    @EzyId
    private String id;
    private String name;
    private String hello = "hello";
    private String world = "world";

}
