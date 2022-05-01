package com.tvd12.ezydata.mongodb.testing.result;

import com.tvd12.ezyfox.database.annotation.EzyQueryResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EzyQueryResult
public class EmployeeIdResult {

    private String employeeId;

}
