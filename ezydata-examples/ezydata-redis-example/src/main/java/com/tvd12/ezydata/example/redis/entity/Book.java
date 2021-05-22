package com.tvd12.ezydata.example.redis.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tvd12.ezyfox.data.annotation.EzyCachedKey;
import com.tvd12.ezyfox.data.annotation.EzyCachedValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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