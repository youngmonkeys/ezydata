package com.tvd12.ezydata.example.redis.entity;

import com.tvd12.ezydata.database.annotation.EzyCachedKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@EzyCachedKey
@AllArgsConstructor
@NoArgsConstructor
public class BookNameAndAuthorId {
    private String bookName;
    private long authorId;
}
