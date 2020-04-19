package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.database.annotation.EzyCollection;
import com.tvd12.ezyfox.annotation.EzyId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EzyCollection("test_mongo_bean_person")
public class Person {

	@EzyId
	private Integer id;
	private String name;
	
}
