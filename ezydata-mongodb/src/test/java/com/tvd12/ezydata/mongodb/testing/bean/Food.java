package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.database.annotation.EzyCollection;
import com.tvd12.ezyfox.annotation.EzyId;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EzyCollection("test_aggregate")
public class Food {

	@EzyId
	private int id;
	private String category;
	private String type;
	private int qty;
	
}
