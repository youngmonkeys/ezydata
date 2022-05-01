package com.tvd12.ezydata.mongodb.testing.result;

import com.tvd12.ezydata.mongodb.testing.bean.DuckId;
import com.tvd12.ezyfox.database.annotation.EzyQueryResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@EzyQueryResult
@NoArgsConstructor
@AllArgsConstructor
public class DuckResult {
    private DuckId id;
    private String description;
}
