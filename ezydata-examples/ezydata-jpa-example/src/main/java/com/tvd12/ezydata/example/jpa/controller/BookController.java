package com.tvd12.ezydata.example.jpa.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tvd12.ezydata.example.jpa.converter.EntityToResponseConverter;
import com.tvd12.ezydata.example.jpa.converter.RequestToEntityConverter;
import com.tvd12.ezydata.example.jpa.entity.Author;
import com.tvd12.ezydata.example.jpa.entity.Book;
import com.tvd12.ezydata.example.jpa.entity.Category;
import com.tvd12.ezydata.example.jpa.repository.AuthorRepository;
import com.tvd12.ezydata.example.jpa.repository.BookRepository;
import com.tvd12.ezydata.example.jpa.repository.CategoryRepository;
import com.tvd12.ezydata.example.jpa.request.AddBookRequest;
import com.tvd12.ezydata.example.jpa.response.BookResponse;
import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.util.Next;
import com.tvd12.ezyhttp.core.exception.HttpBadRequestException;
import com.tvd12.ezyhttp.core.exception.HttpNotFoundException;
import com.tvd12.ezyhttp.server.core.annotation.Controller;
import com.tvd12.ezyhttp.server.core.annotation.DoGet;
import com.tvd12.ezyhttp.server.core.annotation.DoPost;
import com.tvd12.ezyhttp.server.core.annotation.PathVariable;
import com.tvd12.ezyhttp.server.core.annotation.RequestBody;
import com.tvd12.ezyhttp.server.core.annotation.RequestParam;

import lombok.AllArgsConstructor;
import lombok.val;

@AllArgsConstructor
@Controller("/api/v1")
public class BookController {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final EntityToResponseConverter entityToResponseConverter;
    private final RequestToEntityConverter requestToEntityConverter;

    @DoPost("/book/add")
    public BookResponse addBook(@RequestBody AddBookRequest request) {
        Book existedBook = bookRepository.findByNameAndAuthorId(
            request.getBookName(),
            request.getAuthorId()
        );
        if (existedBook != null) {
            throw new HttpBadRequestException(
                "author: " + request.getAuthorId() + 
                " has already registered book: " + request.getBookName()
            );
        }
        Author author = authorRepository.findById(request.getAuthorId());
        if (author == null) {
        	throw new HttpBadRequestException(
                    "author: " + request.getAuthorId() + " not found"
                );
        }
        Category category = categoryRepository.findById(request.getCategoryId());
        if (category == null) {
        	throw new HttpBadRequestException(
                    "category: " + request.getCategoryId() + " not found"
                );
        }

        val book = requestToEntityConverter.toBookEntity(request);
        bookRepository.save(book);
        return entityToResponseConverter.toBookResponse(book, author, category);
    }

    @DoGet("/books/{bookId}")
    public BookResponse getBook(@PathVariable Long bookId) {
        Book book = bookRepository.findById(bookId);
        if(book == null) {
            throw new HttpNotFoundException("not found book with id: " + bookId);
        }
        Author author = authorRepository.findById(book.getAuthorId());
        Category category = categoryRepository.findById(book.getCategoryId());
        return entityToResponseConverter.toBookResponse(
        	book,
            author,
            category
        );
    }

    @DoGet("/books")
    public List<BookResponse> getBooks(
        @RequestParam("lower_than") String lowerThan,
        @RequestParam("upper_than") String upperThan,
        @RequestParam("size") int size
    ) {
        List<Book> books = null;
        if(EzyStrings.isEmpty(upperThan)) {
        	if(EzyStrings.isEmpty(lowerThan)) {
        		books = bookRepository.findBooks(Next.fromSkipLimit(0, size));
        	}
        	else {
        		books = bookRepository.findByNameLt(lowerThan, Next.fromSkipLimit(0, size));
        	}
        }
    	else {
    		books = bookRepository.findByNameGt(upperThan, Next.fromSkipLimit(0, size));
    	}
        List<Long> authorIds = books.stream()
        		.map(it -> it.getAuthorId())
        		.collect(Collectors.toList());
        List<Long> categoryIds = books.stream()
        		.map(it -> it.getCategoryId())
        		.collect(Collectors.toList());
        Map<Long, Author> authors = authorRepository.findListByIds(authorIds)
        	.stream()
        	.collect(Collectors.toMap(it -> it.getId(), it -> it));
        Map<Long, Category> categories = categoryRepository.findListByIds(categoryIds)
    		.stream()
        	.collect(Collectors.toMap(it -> it.getId(), it -> it));
        return entityToResponseConverter.toBooksResponse(
        	books,
            authors,
            categories
        );
    }

    @DoGet("/books/expected-revenue")
    public BigDecimal getExpectedRevenue() {
        return bookRepository.sumPrice().getSum();
    }
}