package com.tvd12.ezydata.example.redis.controller;

import java.util.Map;

import com.tvd12.ezydata.example.redis.entity.Author;
import com.tvd12.ezydata.example.redis.request.AddAuthorRequest;
import com.tvd12.ezydata.redis.EzyRedisAtomicLong;
import com.tvd12.ezydata.redis.EzyRedisProxy;
import com.tvd12.ezyhttp.server.core.annotation.Controller;
import com.tvd12.ezyhttp.server.core.annotation.DoPost;
import com.tvd12.ezyhttp.server.core.annotation.RequestBody;

@Controller("/api/v1/author")
public class AuthorController {

    private final EzyRedisAtomicLong idGentor;
    private final Map<Long, Author> authorMap;
    
    public AuthorController(EzyRedisProxy redisProxy) {
    	this.idGentor = redisProxy.getAtomicLong("author");
    	this.authorMap = redisProxy.getMap("author");
	}

    @DoPost("/add")
    public Author addAuthor(@RequestBody AddAuthorRequest request) {
    	Author author = new Author(
            idGentor.incrementAndGet(),
            request.getAuthorName()
        );
        authorMap.put(author.getId(), author);
        return author;
    }

}