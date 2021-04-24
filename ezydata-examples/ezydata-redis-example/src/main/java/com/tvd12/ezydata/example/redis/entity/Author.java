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
@AllArgsConstructor
@NoArgsConstructor
public class Author {
    @EzyCachedKey
    private long id;
    private String name;
}