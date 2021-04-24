package com.tvd12.ezydata.example.redis.entity;

import com.tvd12.ezydata.database.annotation.EzyCachedKey;
import com.tvd12.ezydata.database.annotation.EzyCachedValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@EzyCachedValue
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @EzyCachedKey
    private Long id;
    private Long categoryId;
    private Long authorId;
    private String name;
    private BigDecimal price;
    private LocalDate releaseDate;
    private LocalDateTime releaseTime;
}