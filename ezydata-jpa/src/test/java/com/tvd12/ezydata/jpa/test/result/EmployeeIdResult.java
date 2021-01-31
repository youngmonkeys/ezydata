package com.tvd12.ezydata.jpa.test.result;

import com.tvd12.ezydata.database.annotation.EzyQueryResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EzyQueryResult
public class EmployeeIdResult {

	private String employeeId;
	
}
