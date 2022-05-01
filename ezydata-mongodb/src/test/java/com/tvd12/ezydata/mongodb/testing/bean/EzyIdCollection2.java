package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.database.annotation.EzyCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EzyCollection
public class EzyIdCollection2 {
    @EzyId(composite = true)
    private EzyIdCompositeId2 id;
    private String value;
}
