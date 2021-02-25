package com.tvd12.ezydata.redis.test.entity;

import com.tvd12.ezydata.redis.annotation.EzyRedisMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EzyRedisMessage(channel = "ezydata_chat_message2")
public class ChatMessage2 {

	private String message;
	
}
