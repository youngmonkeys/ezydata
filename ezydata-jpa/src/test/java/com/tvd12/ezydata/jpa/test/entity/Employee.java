package com.tvd12.ezydata.jpa.test.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ezyfox_jpa_employee")
public class Employee {

	@Id
	private int id;
	private String firstName;
	private String lastName;
	
}