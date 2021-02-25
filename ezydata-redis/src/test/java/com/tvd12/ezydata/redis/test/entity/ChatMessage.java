package com.tvd12.ezydata.redis.test.entity;

import com.tvd12.ezydata.redis.annotation.EzyRedisMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EzyRedisMessage("ezydata_chat_message")
public class ChatMessage {

	private String message;
	
}
