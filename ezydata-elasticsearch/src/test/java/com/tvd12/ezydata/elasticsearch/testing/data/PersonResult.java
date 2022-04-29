package com.tvd12.ezydata.elasticsearch.testing.data;

import java.util.Map;

import com.tvd12.ezydata.elasticsearch.annotation.EzyDataIndexes;
import com.tvd12.ezyfox.annotation.EzyId;
import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfox.data.annotation.EzyIndexedData;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EzyObjectBinding
@EzyIndexedData
@EzyDataIndexes({
    "person"
})
@NoArgsConstructor
public class PersonResult {
    
    @EzyId
    private Map<String, String> name;
    private String email;
    private String phoneNumber;
    private int age;
    
}
