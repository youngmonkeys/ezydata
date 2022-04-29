package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezyfox.database.annotation.EzyCollection;
import com.tvd12.ezyfox.database.annotation.EzyCollectionId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EzyCollection
public class EzyCollectionIdCollection1 {
    @EzyCollectionId
    private EzyCollectionIdCompositeId1 id;
    private String value;
}
