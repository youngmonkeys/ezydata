package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.database.annotation.EzyCollection;
import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.binding.annotation.EzyValue;

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
	
}
