package com.tvd12.kotlin.examples.mongo.controller;

import com.tvd12.ezydata.database.repository.EzyMaxIdRepository;
import com.tvd12.ezyhttp.core.exception.HttpBadRequestException;
import com.tvd12.ezyhttp.server.core.annotation.Controller;
import com.tvd12.ezyhttp.server.core.annotation.DoPost;
import com.tvd12.ezyhttp.server.core.annotation.RequestBody;
import com.tvd12.kotlin.examples.mongo.entity.Category;
import com.tvd12.kotlin.examples.mongo.repository.CategoryRepository;
import com.tvd12.kotlin.examples.mongo.request.AddCategoryRequest;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller("/api/v1/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final EzyMaxIdRepository maxIdRepository;

    @DoPost("/add")
    public Category addCategory(@RequestBody AddCategoryRequest request) {
        Category existedCategory = categoryRepository.findByName(request.getCategoryName());
        if (existedCategory != null) {
            throw new HttpBadRequestException("category named: " + request.getCategoryName() + " existed");
        }
        Category category = new Category(
            maxIdRepository.incrementAndGet("category"),
            request.getCategoryName()
        );
        categoryRepository.save(category);
        return category;
    }

}