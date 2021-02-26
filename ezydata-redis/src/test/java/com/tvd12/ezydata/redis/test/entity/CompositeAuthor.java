package com.tvd12.ezydata.redis.test.entity;

import com.tvd12.ezydata.database.annotation.EzyCachedKey;
import com.tvd12.ezydata.database.annotation.EzyCachedValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EzyCachedValue("ezydata_author3")
public class CompositeAuthor {

	@EzyCachedKey(composite = true)
	private CompositeId id;
	
	private String name;
	
	@Getter
	@Setter
	public static class CompositeId {
		private long id;
	}
	
}
