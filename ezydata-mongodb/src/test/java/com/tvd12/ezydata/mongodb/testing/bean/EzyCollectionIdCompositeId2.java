package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezyfox.database.annotation.EzyCollectionId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EzyCollectionId
public class EzyCollectionIdCompositeId2 {
    private String value;
}

