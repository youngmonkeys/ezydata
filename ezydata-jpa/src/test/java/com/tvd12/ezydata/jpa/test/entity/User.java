package com.tvd12.ezydata.jpa.test.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ezyfox_jpa_user")
public class User {
    @Id
    @Column(name = "userId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullName = "full name";
    private String email = "mail@youngmonkeys.org";
    private String password = "password";
}
