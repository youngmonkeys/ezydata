package com.tvd12.ezydata.hazelcast.testing.service;

import com.tvd12.ezyfox.util.EzyHasIdEntity;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
public class Person implements EzyHasIdEntity<String>, Serializable {
    private static final long serialVersionUID = -3967779291148509736L;

    private String name;
    private int age;

    @Override
    public String getId() {
        return name;
    }
}
