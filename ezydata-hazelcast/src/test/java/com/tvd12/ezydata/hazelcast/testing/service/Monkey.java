package com.tvd12.ezydata.hazelcast.testing.service;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Monkey implements Serializable {
    private static final long serialVersionUID = 8171070638249273626L;

    private String name;
    private int age;

}
