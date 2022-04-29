package com.tvd12.ezydata.redis.test.entity;

import javax.persistence.Id;

import com.tvd12.ezyfox.data.annotation.EzyCachedValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EzyCachedValue("ezydata_author3")
public class Author3 {

    @Id
    private long id;

    private String name;

}
