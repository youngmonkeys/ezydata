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
public class EzyCollectionIdCollection2 {
    @EzyCollectionId
    private EzyCollectionIdCompositeId2 id;
    private String value;
}
