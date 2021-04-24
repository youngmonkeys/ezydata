package com.tvd12.ezydata.example.redis.entity;

import com.tvd12.ezydata.database.annotation.EzyCachedKey;
import com.tvd12.ezydata.database.annotation.EzyCachedValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EzyCachedValue
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @EzyCachedKey
    private Long id;
    private String name;
}