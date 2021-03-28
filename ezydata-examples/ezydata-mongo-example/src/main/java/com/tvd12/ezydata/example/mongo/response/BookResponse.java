package com.tvd12.ezydata.example.mongo.response;

import java.math.BigDecimal;
import java.util.Date;

import com.tvd12.ezydata.example.mongo.entity.Author;
import com.tvd12.ezydata.example.mongo.entity.Category;

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