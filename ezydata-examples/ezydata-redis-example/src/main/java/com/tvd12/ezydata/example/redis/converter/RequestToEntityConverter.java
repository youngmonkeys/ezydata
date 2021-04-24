package com.tvd12.ezydata.example.redis.converter;

import com.tvd12.ezydata.example.common.DateConverter;
import com.tvd12.ezydata.example.redis.entity.Book;
import com.tvd12.ezydata.example.redis.request.AddBookRequest;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;

@EzySingleton
public class RequestToEntityConverter {
	public Book toBookEntity(AddBookRequest request, Long bookId) {
	    return new Book(
	        bookId,
	        request.getCategoryId(),
	        request.getAuthorId(),
	        request.getBookName(),
	        request.getPrice(),
	        DateConverter.toLocalDate(request.getReleaseDate()),
	        DateConverter.toLocalDateTime(request.getReleaseTime())
	    );
	}
}