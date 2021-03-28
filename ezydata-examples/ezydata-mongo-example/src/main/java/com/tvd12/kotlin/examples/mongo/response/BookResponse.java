package com.tvd12.kotlin.examples.mongo.response;

import java.math.BigDecimal;
import java.util.Date;

import com.tvd12.kotlin.examples.mongo.entity.Author;
import com.tvd12.kotlin.examples.mongo.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookResponse {
    private Long bookId;
    private Category category;
    private Author author;
    private String bookName;
    private BigDecimal price;
    private Date releaseTime;
}