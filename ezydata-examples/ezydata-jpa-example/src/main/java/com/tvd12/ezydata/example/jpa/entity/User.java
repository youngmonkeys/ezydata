package com.tvd12.ezydata.example.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tvd12.ezydata.example.jpa.constant.UserType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "jpa_example_user")
public class User {
	
	@Id
	@Column(name = "userId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String email;
	private UserType type;
	private String fullName;
	private String password;
	
}