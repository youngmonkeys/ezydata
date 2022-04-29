package com.tvd12.ezydata.mongodb.testing.bean;

import java.util.Date;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EzyCollection("test_mongodb_category")
public class Category {

    @EzyId
    private String name;
    private String value;
    private Date createdDate = new Date();

    public Category() {}

    public Category(String value) {
        this.value = value;
    }

}
