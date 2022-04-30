package com.tvd12.ezydata.jpa.test.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "ezyfox_jpa_monkey")
public class Monkey {
    private String monkeyId;

    @Id
    public String getId() {
        return monkeyId;
    }

    @Id
    public void setId(String id) {
        this.monkeyId = id;
    }
}
