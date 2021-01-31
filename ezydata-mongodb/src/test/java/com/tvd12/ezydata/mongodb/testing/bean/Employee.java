package com.tvd12.ezydata.mongodb.testing.bean;

import com.tvd12.ezydata.database.annotation.EzyCollection;
import com.tvd12.ezyfox.annotation.EzyId;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EzyCollection("test_mongo_employee")
public class Employee {

	@EzyId
	private String employeeId;
	private String firstName;
	private String lastName;
	private String email = "dzung@youngmokeys.org";
	private String phoneNumber = "123456789";
	private String bankAccountNo = "abcdefgh";
	
}