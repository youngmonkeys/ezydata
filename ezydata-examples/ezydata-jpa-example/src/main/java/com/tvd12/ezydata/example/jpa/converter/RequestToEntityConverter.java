package com.tvd12.ezydata.example.jpa.converter;

import com.tvd12.ezydata.example.common.DateConverter;
import com.tvd12.ezydata.example.jpa.entity.Book;
import com.tvd12.ezydata.example.jpa.request.AddBookRequest;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;

@EzySingleton
public class RequestToEntityConverter {
	public Book toBookEntity(AddBookRequest request) {
	    return new Book(
	        request.getCategoryId(),
	        request.getAuthorId(),
	        request.getBookName(),
	        request.getPrice(),
	        DateConverter.toLocalDate(request.getReleaseDate()),
	        DateConverter.toLocalDateTime(request.getReleaseTime())
	    );
	}
}