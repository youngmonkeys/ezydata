package com.tvd12.ezydata.example.jpa.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long categoryId;
    private Long authorId;
    private String name;
    private BigDecimal price;
    private LocalDate releaseDate;
    private LocalDateTime releaseTime;
    
    public Book(
		Long categoryId,
	    Long authorId,
	    String name,
	    BigDecimal price,
	    LocalDate releaseDate,
	    LocalDateTime releaseTime
    ) {
    	this.categoryId = categoryId;
    	this.authorId = authorId;
    	this.name = name;
    	this.price = price;
    	this.releaseDate = releaseDate;
    	this.releaseTime = releaseTime;
    }
}