package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezyfox.database.annotation.EzyCollection;
import com.tvd12.ezyfox.database.annotation.EzyCollectionId;
import lombok.*;

@Getter
@Setter
@ToString
@EzyCollection("test_mongo_duck")
@NoArgsConstructor
@AllArgsConstructor
public class Duck {
    @EzyCollectionId(composite = true)
    private DuckId id;
    private int age;
    private String description;
}
