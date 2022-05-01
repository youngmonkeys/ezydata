package com.tvd12.ezydata.mongodb.testing.result;

import com.tvd12.ezydata.mongodb.testing.bean.DuckId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DuckResult {
    private DuckId id;
    private String description;
}
