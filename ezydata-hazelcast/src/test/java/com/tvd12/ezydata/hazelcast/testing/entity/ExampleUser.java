package com.tvd12.ezydata.hazelcast.testing.entity;

import com.tvd12.ezyfox.util.EzyHasIdEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExampleUser implements EzyHasIdEntity<String>, Serializable {
    private static final long serialVersionUID = 975411654175906424L;

    private String username;

    @Override
    public String getId() {
        return username;
    }
}
