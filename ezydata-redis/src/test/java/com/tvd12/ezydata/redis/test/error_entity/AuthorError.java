package com.tvd12.ezydata.redis.test.error_entity;

import com.tvd12.ezyfox.data.annotation.EzyCachedValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EzyCachedValue("ezydata_author_error")
public class AuthorError {

    private long id;

    private String name;

}
