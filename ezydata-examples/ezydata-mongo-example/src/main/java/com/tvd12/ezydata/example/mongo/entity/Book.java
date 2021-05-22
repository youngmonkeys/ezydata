package com.tvd12.ezydata.example.mongo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EzyCollection
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @EzyId
    private Long id;
    private Long categoryId;
    private Long authorId;
    private String name;
    private BigDecimal price;
    private LocalDate releaseDate;
    private LocalDateTime releaseTime;
}