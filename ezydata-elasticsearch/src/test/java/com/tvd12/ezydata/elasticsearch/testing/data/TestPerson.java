package com.tvd12.ezydata.elasticsearch.testing.data;

import com.tvd12.ezydata.elasticsearch.annotation.EzyDataIndexes;
import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfox.data.annotation.EzyIndexedData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EzyObjectBinding
@EzyIndexedData
@EzyDataIndexes({
	"person"
})
@NoArgsConstructor
@AllArgsConstructor
public class TestPerson {
	
	@EzyId
	private String email;
	private Name name;
	private String phoneNumber;
	private int age;
	
	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	@EzyObjectBinding
	public static class Name {
		private String vietnamese;
		private String english;
	}
	
}
