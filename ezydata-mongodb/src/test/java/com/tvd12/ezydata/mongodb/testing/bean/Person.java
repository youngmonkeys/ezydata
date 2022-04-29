package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.binding.annotation.EzyValue;
import com.tvd12.ezyfox.database.annotation.EzyCollection;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EzyCollection("test_mongo_bean_person")
public class Person {

    @EzyId
    private Integer id;
    @EzyValue("personName")
    private String name;
    
    @EzyId
    public Integer getId() {
        return id;
    }
    
    @EzyId
    public void setId(Integer id) {
        this.id = id;
    }
    
}
