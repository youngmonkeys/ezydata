package com.tvd12.ezydata.redis.test.entity;

import com.tvd12.ezyfox.message.annotation.EzyMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EzyMessage(channel = "ezydata_chat_message4")
public class ChatMessage4 {

	private String message;
	
}
