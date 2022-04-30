package com.tvd12.ezydata.elasticsearch.testing.data;

import com.tvd12.ezydata.elasticsearch.annotation.EzyDataIndexes;
import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.data.annotation.EzyIndexedData;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EzyIndexedData
@EzyDataIndexes({
    "test1", "test2"
})
public class Person1 {
    @EzyId
    private long id;
}
