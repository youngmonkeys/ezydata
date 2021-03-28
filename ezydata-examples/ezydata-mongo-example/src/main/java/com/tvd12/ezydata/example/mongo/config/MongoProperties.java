package com.tvd12.ezydata.example.mongo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MongoProperties {
	private String uri;
    private String database;
}