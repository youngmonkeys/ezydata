package com.tvd12.ezydata.example.redis.request;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBookRequest {
    private Long categoryId;
    private Long authorId;
    private String bookName;
    private BigDecimal price;
    private Date releaseDate;
    private Date releaseTime;
}