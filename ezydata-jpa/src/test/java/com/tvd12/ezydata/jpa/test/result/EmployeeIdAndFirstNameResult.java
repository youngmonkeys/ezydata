package com.tvd12.ezydata.jpa.test.result;

import com.tvd12.ezyfox.database.annotation.EzyQueryResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EzyQueryResult
public class EmployeeIdAndFirstNameResult {
    private String employeeId;
    private String firstName;
}
