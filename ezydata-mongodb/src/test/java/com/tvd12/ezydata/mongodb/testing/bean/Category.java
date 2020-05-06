package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.database.annotation.EzyCollection;
import com.tvd12.ezyfox.annotation.EzyId;

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
	
	public Category() {}
	
	public Category(String value) {
		this.value = value;
	}
	
}
