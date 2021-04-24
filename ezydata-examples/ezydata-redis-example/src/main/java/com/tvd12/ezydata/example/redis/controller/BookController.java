package com.tvd12.ezydata.example.redis.controller;

import java.util.Map;

import com.tvd12.ezydata.example.redis.converter.EntityToResponseConverter;
import com.tvd12.ezydata.example.redis.converter.RequestToEntityConverter;
import com.tvd12.ezydata.example.redis.entity.Author;
import com.tvd12.ezydata.example.redis.entity.Book;
import com.tvd12.ezydata.example.redis.entity.BookNameAndAuthorId;
import com.tvd12.ezydata.example.redis.entity.Category;
import com.tvd12.ezydata.example.redis.request.AddBookRequest;
import com.tvd12.ezydata.example.redis.response.BookResponse;
import com.tvd12.ezydata.redis.EzyRedisAtomicLong;
import com.tvd12.ezydata.redis.EzyRedisProxy;
import com.tvd12.ezyhttp.core.exception.HttpBadRequestException;
import com.tvd12.ezyhttp.core.exception.HttpNotFoundException;
import com.tvd12.ezyhttp.server.core.annotation.Controller;
import com.tvd12.ezyhttp.server.core.annotation.DoGet;
import com.tvd12.ezyhttp.server.core.annotation.DoPost;
import com.tvd12.ezyhttp.server.core.annotation.PathVariable;
import com.tvd12.ezyhttp.server.core.annotation.RequestBody;

import lombok.val;

@Controller("/api/v1")
public class BookController {

    private final EzyRedisAtomicLong idGentor;
    private final Map<Long, Book> bookMap;
    private final Map<Long, Author> authorMap;
    private final Map<Long, Category> categoryMap;
    private final Map<BookNameAndAuthorId, Long> bookIdByNameAndAuthorIdMap;
    private final EntityToResponseConverter entityToResponseConverter;
    private final RequestToEntityConverter requestToEntityConverter;
    
    public BookController(
		EzyRedisProxy redisProxy,
		EntityToResponseConverter entityToResponseConverter,
		RequestToEntityConverter requestToEntityConverter
    ) {
    	this.idGentor = redisProxy.getAtomicLong("book");
    	this.bookMap = redisProxy.getMap("book");
    	this.authorMap = redisProxy.getMap("author");
    	this.categoryMap = redisProxy.getMap("category");
    	this.bookIdByNameAndAuthorIdMap = redisProxy.getMap(
	        "bookIdByNameAndAuthorId",
	        BookNameAndAuthorId.class,
	        Long.class
    	);
    	this.entityToResponseConverter = entityToResponseConverter;
    	this.requestToEntityConverter = requestToEntityConverter;
    }

    @DoPost("/book/add")
    public BookResponse addBook(@RequestBody AddBookRequest request) {
    	BookNameAndAuthorId bookNameAndAuthorId = new BookNameAndAuthorId(
            request.getBookName(),
            request.getAuthorId()
        );
        Long existedBookId = bookIdByNameAndAuthorIdMap.get(bookNameAndAuthorId);
        if (existedBookId != null) {
            throw new HttpBadRequestException(
                "author: " + request.getAuthorId() + 
                " has already registered book: " + request.getBookName()
            );
        }
        Author author = authorMap.get(request.getAuthorId());
        if(author == null) {
            throw new HttpBadRequestException(
            		"author: " + request.getAuthorId() + " not found"
            );
        }

        Category category = categoryMap.get(request.getCategoryId());
        if(category == null) {
            throw new HttpBadRequestException(
                "category: " + request.getCategoryId() + " not found"
            );
        }

        val bookId = idGentor.incrementAndGet();
        val book = requestToEntityConverter.toBookEntity(request, bookId);
        bookMap.put(book.getId(), book);
        bookIdByNameAndAuthorIdMap.put(bookNameAndAuthorId, bookId);
        return entityToResponseConverter.toBookResponse(book, author, category);
    }

    @DoGet("/books/{bookId}")
    public BookResponse getBook(@PathVariable Long bookId) {
        Book book = bookMap.get(bookId);
        if(book == null) {
            throw new HttpNotFoundException("not found book with id: " + bookId);
        }
        Author author = authorMap.get(book.getAuthorId());
        Category category = categoryMap.get(book.getCategoryId());
        return entityToResponseConverter.toBookResponse(
        	book,
        	author,
        	category
        );
    }
}