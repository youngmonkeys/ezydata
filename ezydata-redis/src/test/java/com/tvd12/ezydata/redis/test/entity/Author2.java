package com.tvd12.ezydata.redis.test.entity;

import com.tvd12.ezydata.database.annotation.EzyCachedValue;
import com.tvd12.ezyfox.annotation.EzyId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EzyCachedValue("ezydata_author2")
public class Author2 {

	@EzyId
	private long id;
	
	private String name;
	
}
