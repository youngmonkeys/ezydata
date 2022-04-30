package com.tvd12.ezydata.jpa.test.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "ezyfox_jpa_employee")
public class Employee {
    @Id
    private String employeeId;
    private String firstName;
    private String lastName;
    private String email = "dzung@youngmokeys.org";
    private String phoneNumber = "123456789";
    private String bankAccountNo = "abcdefgh";
}
