package com.tvd12.ezydata.example.mongo.controller;

import com.tvd12.ezydata.database.repository.EzyMaxIdRepository;
import com.tvd12.ezydata.example.mongo.entity.Author;
import com.tvd12.ezydata.example.mongo.repository.AuthorRepository;
import com.tvd12.ezydata.example.mongo.request.AddAuthorRequest;
import com.tvd12.ezyhttp.server.core.annotation.Controller;
import com.tvd12.ezyhttp.server.core.annotation.DoPost;
import com.tvd12.ezyhttp.server.core.annotation.RequestBody;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller("/api/v1/author")
public class AuthorController {
	private final AuthorRepository authorRepository;
    private final EzyMaxIdRepository maxIdRepository;
    
    @DoPost("/add")
    public Author addAuthor(@RequestBody AddAuthorRequest request) {
    	Author author = new Author(
            maxIdRepository.incrementAndGet("author"),
            request.getAuthorName()
        );
        authorRepository.save(author);
        return author;
    }

}