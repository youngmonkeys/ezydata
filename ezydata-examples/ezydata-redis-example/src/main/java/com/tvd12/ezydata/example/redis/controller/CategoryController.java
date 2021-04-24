package com.tvd12.ezydata.example.redis.controller;

import java.util.Map;

import com.tvd12.ezydata.example.redis.entity.Category;
import com.tvd12.ezydata.example.redis.request.AddCategoryRequest;
import com.tvd12.ezydata.redis.EzyRedisAtomicLong;
import com.tvd12.ezydata.redis.EzyRedisProxy;
import com.tvd12.ezyhttp.core.exception.HttpBadRequestException;
import com.tvd12.ezyhttp.server.core.annotation.Controller;
import com.tvd12.ezyhttp.server.core.annotation.DoPost;
import com.tvd12.ezyhttp.server.core.annotation.RequestBody;

@Controller("/api/v1/category")
public class CategoryController {
    private final EzyRedisAtomicLong idGentor;
    private final Map<Long, Category> categoryMap;
    private final Map<String, Long> categoryIdByNameMap;
        
    public CategoryController(EzyRedisProxy redisProxy) {
    	this.idGentor = redisProxy.getAtomicLong("category");
    	this.categoryMap = redisProxy.getMap("category");
    	this.categoryIdByNameMap =
    		        redisProxy.getMap("categoryIdByName", String.class, Long.class);
	}

    @DoPost("/add")
    public Category addCategory(@RequestBody AddCategoryRequest request) {
        Long existedCategoryId = categoryIdByNameMap.get(request.getCategoryName());
        if (existedCategoryId != null) {
            throw new HttpBadRequestException(
            	"category named: " + request.getCategoryName() + " existed"
            );
        }
        Category category = new Category(
            idGentor.incrementAndGet(),
            request.getCategoryName()
        );
        categoryMap.put(category.getId(), category);
        categoryIdByNameMap.put(category.getName(), category.getId());
        return category;
    }

}