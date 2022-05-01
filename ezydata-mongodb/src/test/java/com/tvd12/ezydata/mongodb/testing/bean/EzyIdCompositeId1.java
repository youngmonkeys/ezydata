package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezyfox.annotation.EzyId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@EzyId
@AllArgsConstructor
@NoArgsConstructor
public class EzyIdCompositeId1 {
    private String value;
}

