package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EzyObjectBinding
@NoArgsConstructor
@AllArgsConstructor
public class DuckId {

	private int type;
	private String name;
	
}
