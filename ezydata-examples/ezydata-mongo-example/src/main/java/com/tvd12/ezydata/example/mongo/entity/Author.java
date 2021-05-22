package com.tvd12.ezydata.example.mongo.entity;

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
public class Author {
    @EzyId
    private long id;
    private String name;
}