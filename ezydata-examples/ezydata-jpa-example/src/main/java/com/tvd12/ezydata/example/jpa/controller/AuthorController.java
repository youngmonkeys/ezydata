package com.tvd12.ezydata.example.jpa.controller;

import com.tvd12.ezydata.example.jpa.entity.Author;
import com.tvd12.ezydata.example.jpa.repository.AuthorRepository;
import com.tvd12.ezydata.example.jpa.request.AddAuthorRequest;
import com.tvd12.ezyhttp.server.core.annotation.Controller;
import com.tvd12.ezyhttp.server.core.annotation.DoPost;
import com.tvd12.ezyhttp.server.core.annotation.RequestBody;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller("/api/v1/author")
public class AuthorController {
	private final AuthorRepository authorRepository;
    
    @DoPost("/add")
    public Author addAuthor(@RequestBody AddAuthorRequest request) {
    	Author author = new Author(
            request.getAuthorName()
        );
        authorRepository.save(author);
        return author;
    }

}