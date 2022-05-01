package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EzyCollection("test_mongo_bean_chickend")
public class Chicken {

    @EzyId
    private String _id;
    private String name;

}
