package com.tvd12.ezydata.hazelcast.testing.service;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Chicken implements Serializable {
    private static final long serialVersionUID = 8171070638249273626L;
    
    private String name;
    private int age;

}
