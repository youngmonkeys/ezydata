package com.tvd12.ezydata.redis.test.entity;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EzyObjectBinding
public class ChatMessage3 {

	private String message;
	
}
